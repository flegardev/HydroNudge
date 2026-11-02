package app.hydronudge.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.hydronudge.HydroApplication
import app.hydronudge.data.local.DrinkEntry
import app.hydronudge.data.local.FavoriteDrink
import app.hydronudge.data.preferences.HydroSettings
import app.hydronudge.domain.*
import app.hydronudge.data.BackupCodec
import android.net.Uri
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import java.time.*

data class HydroState(
    val entries: List<DrinkEntry> = emptyList(),
    val favorites: List<FavoriteDrink> = emptyList(),
    val settings: HydroSettings = HydroSettings(),
    val date: LocalDate = LocalDate.now(),
    val zone: ZoneId = ZoneId.systemDefault(),
    val ready: Boolean = false
) {
    val today: List<DrinkEntry> get() { val w = dayWindow(date, zone); return entries.filter { it.timestamp >= w.start && it.timestamp < w.endExclusive } }
    val totalMl get() = today.sumOf { it.amountMl.toLong() }
}
data class Feedback(val message: String, val undoEntry: DrinkEntry? = null, val undoDeletes: Boolean = false)
class HydroViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application as HydroApplication
    private val clock = flow {
        while(currentCoroutineContext().isActive) {
            val now = ZonedDateTime.now()
            emit(now.toLocalDate() to now.zone)
            val midnight = now.toLocalDate().plusDays(1).atStartOfDay(now.zone)
            delay(minOf(30000L,Duration.between(now,midnight).toMillis().coerceAtLeast(1)))
        }
    }
    private val events = Channel<Feedback>(Channel.UNLIMITED)
    val feedback = events.receiveAsFlow()
    val state = combine(app.repository.entries, app.repository.favorites, app.preferences.settings, clock) { entries, favorites, settings, now ->
        HydroState(entries, favorites, settings, now.first, now.second, true)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HydroState())

    fun quickAdd(ml: Int) = save(DrinkEntry(name = "Water", category = DrinkCategory.WATER.name, amountMl = ml))
    fun save(entry: DrinkEntry, editing: Boolean = false) = perform {
        if (editing) app.repository.edit(entry) else app.repository.add(entry)
        events.send(Feedback(if (editing) "Drink updated" else "Drink added", if (editing) null else entry))
    }
    fun delete(entry: DrinkEntry) = perform { app.repository.delete(entry.id); events.send(Feedback("Drink deleted", entry, true)) }
    fun undo(event: Feedback) = perform { event.undoEntry?.let { if (event.undoDeletes) app.repository.restore(it) else app.repository.delete(it.id) } }
    fun saveSettings(settings: HydroSettings) = perform { app.preferences.save(settings) }
    fun favorite(value: FavoriteDrink) = perform { app.repository.saveFavorite(value); events.send(Feedback("Favorite saved")) }
    fun removeFavorite(value: FavoriteDrink) = perform { app.repository.deleteFavorite(value.id) }
    fun clear() = perform { app.repository.clearAll(); app.preferences.save(HydroSettings()); events.send(Feedback("All local HydroNudge data deleted")) }
    fun export(uri: Uri) = perform {
        withContext(Dispatchers.IO) {
            val json = BackupCodec.encode(app.repository.snapshot())
            val stream = app.contentResolver.openOutputStream(uri,"wt") ?: error("Could not open the export file.")
            stream.bufferedWriter(Charsets.UTF_8).use { it.write(json) }
        }
        events.send(Feedback("Drink backup exported"))
    }
    fun import(uri: Uri) = perform {
        val count = withContext(Dispatchers.IO) {
            val stream = app.contentResolver.openInputStream(uri) ?: error("Could not open the backup.")
            val limit = 32 * 1024 * 1024
            val bytes = stream.use { input ->
                val output = java.io.ByteArrayOutputStream()
                val buffer = ByteArray(8192)
                while(true) {
                    val count = input.read(buffer)
                    if(count < 0) break
                    require(output.size()+count <= limit) { "This backup exceeds the 32 MiB safe import size." }
                    output.write(buffer,0,count)
                }
                output.toByteArray()
            }
            app.repository.merge(BackupCodec.decode(bytes.toString(Charsets.UTF_8)))
        }
        events.send(Feedback("Imported $count new drinks; existing records kept"))
    }
    private fun perform(block: suspend () -> Unit) { viewModelScope.launch { try { block() } catch (e: CancellationException) { throw e } catch (e: Exception) { events.send(Feedback(e.message ?: "Unable to save. Please try again.")) } } }
}

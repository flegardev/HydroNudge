package app.hydronudge

import android.app.Application
import androidx.room.Room
import app.hydronudge.data.*
import app.hydronudge.data.local.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk=[35],application=Application::class)
class BackupTest {
    private val entry = DrinkEntry(id="saved-id",name="Coffee with milk",category="COFFEE",amountMl=330,note="A \"quoted\" note\nwith a new line")
    private val backup = DrinkBackup(listOf(entry),listOf(FavoriteDrink(id="favorite",name="Morning coffee",category="COFFEE",amountMl=250)))
    @Test fun unknownLegacyIdentifiersSurviveMergeAndExport() = runBlocking {
        val db = Room.inMemoryDatabaseBuilder(RuntimeEnvironment.getApplication(),HydroDatabase::class.java).build()
        try {
            val repository = DrinkRepository(db.drinks())
            val legacy = backup.copy(entries=listOf(entry.copy(category="LEGACY_UNKNOWN")))
            repository.merge(BackupCodec.decode(BackupCodec.encode(legacy)))
            assertEquals(legacy,repository.snapshot())
            try { repository.add(entry); fail("Non-water write accepted") } catch (_:IllegalArgumentException) { }
            assertEquals(legacy,repository.snapshot())
        } finally { db.close() }
    }
    @Test fun roundTripPreservesAllFields() { assertEquals(backup,BackupCodec.decode(BackupCodec.encode(backup))) }
    @Test(expected=IllegalArgumentException::class) fun rejectsUnsupportedSchema() { BackupCodec.decode(BackupCodec.encode(backup).replace("\"schemaVersion\": 1","\"schemaVersion\": 2")) }
    @Test(expected=IllegalArgumentException::class) fun rejectsDuplicateIds() { BackupCodec.decode(BackupCodec.encode(backup.copy(entries=listOf(entry,entry)))) }
    @Test(expected=IllegalArgumentException::class) fun rejectsFractionalVolume() { BackupCodec.decode(BackupCodec.encode(backup).replace("\"amountMl\": 330","\"amountMl\": 330.5")) }
    @Test fun mergeIsIdempotentAndInvalidImportLeavesDatabaseUntouched() = runBlocking {
        val db = Room.inMemoryDatabaseBuilder(RuntimeEnvironment.getApplication(),HydroDatabase::class.java).build()
        try {
            val repository=DrinkRepository(db.drinks())
            assertEquals(1,repository.merge(backup)); assertEquals(0,repository.merge(backup))
            assertEquals(backup,repository.snapshot())
            val invalid=backup.copy(entries=listOf(entry.copy(id="new"),entry.copy(id="bad",amountMl=-1)))
            try { repository.merge(invalid); fail("Invalid data accepted") } catch (_:IllegalArgumentException) { }
            assertEquals(backup,repository.snapshot())
            repository.clearAll(); assertEquals(DrinkBackup(emptyList(),emptyList()),repository.snapshot())
        } finally {db.close()}
    }
}

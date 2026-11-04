package app.hydronudge

import android.app.Application
import androidx.room.Room
import app.hydronudge.data.*
import app.hydronudge.data.local.*
import app.hydronudge.data.preferences.*
import app.hydronudge.domain.VolumeUnit
import app.hydronudge.domain.DrinkCategory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], application = Application::class)
class LocalStorageTest {
    @Test fun roomPersistsEditsDeletesAndPreventsDuplicateActions() = runBlocking {
        val context = RuntimeEnvironment.getApplication()
        val name = "test-${System.nanoTime()}.db"
        fun open() = Room.databaseBuilder(context,HydroDatabase::class.java,name).build()
        var db = open()
        try {
            var repository = DrinkRepository(db.drinks())
            val drink = DrinkEntry(id="action",name="Water",category="WATER",amountMl=250)
            assertTrue(repository.add(drink)); assertFalse(repository.add(drink))
            repository.add(drink.copy(id="coffee",name="Water",category="WATER",amountMl=150))
            assertEquals(400,repository.all().sumOf { it.amountMl })
            db.close(); db = open(); repository = DrinkRepository(db.drinks())
            assertEquals(2,repository.all().size)
            repository.edit(drink.copy(amountMl=500)); assertEquals(650,repository.all().sumOf { it.amountMl })
            repository.delete(drink.id); assertEquals(150,repository.all().sumOf { it.amountMl })
            repository.deleteAll(); assertTrue(repository.all().isEmpty())
        } finally { db.close(); context.deleteDatabase(name) }
    }
    @Test fun datastorePersistsPreferences() = runBlocking {
        val context = RuntimeEnvironment.getApplication()
        val prefs = HydroPreferences(context); val original = prefs.settings.first()
        try {
            val value = HydroSettings(goalMl=3100,unit=VolumeUnit.US_OZ,theme=ThemeMode.DARK,reminders=true,intervalMinutes=45,wakeMinute=600,style=ReminderStyle.FUNNY,statusNotification=true)
            prefs.save(value); assertEquals(value,HydroPreferences(context).settings.first())
            prefs.dismissStatus()
            assertTrue(HydroPreferences(context).settings.first().statusDismissed)
        } finally { prefs.save(original) }
    }
    @Test fun legacyCategoriesRemainInStorage() = runBlocking {
        val db=Room.inMemoryDatabaseBuilder(RuntimeEnvironment.getApplication(),HydroDatabase::class.java).build()
        try {
            val repository=DrinkRepository(db.drinks())
            DrinkCategory.entries.forEach { category -> repository.merge(DrinkBackup(listOf(DrinkEntry(name=category.label,category=category.name,amountMl=100)),emptyList())) }
            assertEquals(12,repository.all().size)
            assertEquals(1200,repository.all().sumOf {it.amountMl})
        } finally { db.close() }
    }
}

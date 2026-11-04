package app.hydronudge

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.hydronudge.data.*
import app.hydronudge.data.local.*
import app.hydronudge.data.preferences.*
import app.hydronudge.domain.VolumeUnit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StorageTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()
    @Test fun databaseSurvivesReopenAndEditsDeletesAndDeduplicates() = runBlocking {
        val name = "test-${System.nanoTime()}.db"
        fun open() = Room.databaseBuilder(context, HydroDatabase::class.java,name).build()
        var db = open()
        try {
            var repository = DrinkRepository(db.drinks())
            val drink = DrinkEntry(id = "action-token",name = "Water",category = "WATER",amountMl = 250)
            assertTrue(repository.add(drink)); assertFalse(repository.add(drink))
            repository.add(drink.copy(id = "second",name = "Water",category = "WATER",amountMl = 150))
            assertEquals(400,repository.all().sumOf { it.amountMl })
            db.close(); db = open(); repository = DrinkRepository(db.drinks())
            assertEquals(2, repository.all().size)
            repository.edit(drink.copy(amountMl = 500)); assertEquals(650,repository.all().sumOf { it.amountMl })
            repository.delete(drink.id); assertEquals(150,repository.all().sumOf { it.amountMl })
            repository.deleteAll(); assertTrue(repository.all().isEmpty())
        } finally { db.close(); context.deleteDatabase(name) }
    }
    @Test fun preferencesPersistAcrossRepositoryInstances() = runBlocking {
        val prefs = HydroPreferences(context); val original = prefs.settings.first()
        try {
            val expected = HydroSettings(goalMl = 3100,unit = VolumeUnit.US_OZ,quickAmounts = listOf(100,200,300,400),theme = ThemeMode.DARK,dynamicColors = true)
            prefs.save(expected)
            assertEquals(expected,HydroPreferences(context).settings.first())
        } finally { prefs.save(original) }
    }
}

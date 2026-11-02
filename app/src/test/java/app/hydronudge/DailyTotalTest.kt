package app.hydronudge

import app.hydronudge.data.local.DrinkEntry
import app.hydronudge.ui.HydroState
import app.hydronudge.domain.dayWindow
import org.junit.Assert.*
import org.junit.Test
import java.time.*

class DailyTotalTest {
    @Test fun todayIncludesAllFluidsAndExcludesNextMidnight() {
        val date = LocalDate.of(2026,10,25); val zone = ZoneId.of("Europe/Ljubljana")
        val window = dayWindow(date,zone)
        fun drink(id: String,category: String,ml: Int,time: Long) = DrinkEntry(id,category,category,ml,time)
        val entries = listOf(drink("a","MILK",250,window.start),drink("b","COFFEE",150,window.endExclusive-1),drink("c","PROTEIN",330,window.start+3600000),drink("d","WATER",500,window.endExclusive))
        assertEquals(730L,HydroState(entries=entries,date=date,zone=zone).totalMl)
        assertEquals(500L,HydroState(entries=entries,date=date.plusDays(1),zone=zone).totalMl)
    }
    @Test fun totalsUseLongArithmetic() {
        val now = System.currentTimeMillis()
        val entries = List(110000) { DrinkEntry(it.toString(),"Water","WATER",20000,now) }
        assertEquals(2200000000L,HydroState(entries=entries).totalMl)
    }
}

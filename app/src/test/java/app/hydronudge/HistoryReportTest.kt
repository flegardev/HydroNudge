package app.hydronudge

import app.hydronudge.data.local.DrinkEntry
import app.hydronudge.domain.*
import org.junit.Assert.*
import org.junit.Test
import java.time.*

class HistoryReportTest {
    @Test fun reportUsesRecordedDaysAndCalendarGroupingOverDst() {
        val zone=ZoneId.of("Europe/Ljubljana"); val start=LocalDate.of(2026,10,24)
        fun entry(id: String,date: LocalDate,ml: Int,category: String)=DrinkEntry(id,category,category,ml,date.atStartOfDay(zone).toInstant().toEpochMilli())
        val records=listOf(entry("a",start,250,"COFFEE"),entry("b",start.plusDays(1),2000,"MILK"),entry("c",start.plusDays(1),500,"WATER"),entry("outside",start.plusDays(7),500,"WATER"))
        val report=historyReport(records,start,start.plusDays(6),zone,2500)
        assertEquals(500L,report.totalMl); assertEquals(1,report.recordedDays); assertEquals(500L,report.averageRecordedDayMl)
        assertEquals(0,report.goalDays); assertEquals(listOf(0L,500L,0L,0L,0L,0L,0L),report.dailyMl)
        assertTrue(report.entries.all { it.isWater() })
    }
}

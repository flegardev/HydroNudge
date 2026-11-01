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
        assertEquals(2750L,report.totalMl); assertEquals(2,report.recordedDays); assertEquals(1375L,report.averageRecordedDayMl)
        assertEquals(1,report.goalDays); assertEquals(listOf(250L,2500L,0L,0L,0L,0L,0L),report.dailyMl)
        assertEquals(2000L,report.categoryMl["MILK"])
    }
}

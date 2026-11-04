package app.hydronudge.domain

import app.hydronudge.data.local.DrinkEntry
import java.time.LocalDate
import java.time.ZoneId

data class HistoryReport(
    val entries: List<DrinkEntry>, val totalMl: Long, val recordedDays: Int,
    val averageRecordedDayMl: Long, val goalDays: Int, val dailyMl: List<Long>
)

fun historyReport(records: List<DrinkEntry>, start: LocalDate, end: LocalDate, zone: ZoneId, goalMl: Int): HistoryReport {
    require(!end.isBefore(start))
    val windowStart=dayWindow(start,zone).start
    val windowEnd=dayWindow(end,zone).endExclusive
    val entries=records.filter { it.isWater() && it.timestamp >= windowStart && it.timestamp < windowEnd }
    val days=entries.groupBy { localDay(it.timestamp,zone) }.mapValues { (_,drinks) -> drinks.sumOf { it.amountMl.toLong() } }
    val total=entries.sumOf { it.amountMl.toLong() }
    return HistoryReport(entries,total,days.size,if(days.isEmpty()) 0 else total/days.size,
        if (goalMl > 0) days.values.count { it >= goalMl } else 0,generateSequence(start) { it.plusDays(1) }.takeWhile { !it.isAfter(end) }.map { days[it] ?: 0L }.toList())
}

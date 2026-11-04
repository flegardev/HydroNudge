package app.hydronudge.domain

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale

enum class DrinkCategory(val label: String, val symbol: String) {
    WATER("Water", "◉"), MILK("Milk", "◌"), COFFEE("Coffee", "☕"), TEA("Tea", "♧"),
    PROTEIN("Protein shake", "◈"), ENERGY("Energy drink", "ϟ"), SODA("Soda", "○"),
    JUICE("Fruit juice", "◐"), SMOOTHIE("Smoothie", "✿"), SPORTS("Sports drink", "◇"),
    SOUP("Soup or broth", "≈"), CUSTOM("Custom drink", "✦")
}

enum class VolumeUnit(val label: String, val millilitres: Double) {
    ML("mL", 1.0), US_OZ("US fl oz", 29.5735295625);
    fun toMl(value: Double): Int = kotlin.math.round(value * millilitres).toInt()
    fun format(ml: Long): String = if (this == ML) String.format(Locale.getDefault(), "%,d mL", ml)
        else String.format(Locale.getDefault(), "%.1f US fl oz", ml / millilitres)
    fun input(ml: Int): String = if (this == ML) ml.toString()
        else String.format(Locale.ROOT, "%.2f", ml / millilitres)
}

data class DayWindow(val start: Long, val endExclusive: Long)
fun dayWindow(date: LocalDate, zone: ZoneId): DayWindow = DayWindow(
    date.atStartOfDay(zone).toInstant().toEpochMilli(),
    date.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli()
)
fun localDay(epochMillis: Long, zone: ZoneId): LocalDate = Instant.ofEpochMilli(epochMillis).atZone(zone).toLocalDate()
fun goalPercent(totalMl: Long, goalMl: Int): Long = if (goalMl > 0) (totalMl * 100L / goalMl) else 0
fun validateVolume(ml: Int) { require(ml in 1..20000) { "Enter a volume between 1 and 20,000 mL." } }

/** Exact legacy identifier: unknown categories never become water. */
fun app.hydronudge.data.local.DrinkEntry.isWater(): Boolean = category == "WATER"

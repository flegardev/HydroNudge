package app.hydronudge

import app.hydronudge.domain.*
import org.junit.Assert.*
import org.junit.Test
import java.time.*

class HydrationTest {
    @Test fun percentDoesNotHideOverflow() { assertEquals(140L, goalPercent(3500,2500)); assertEquals(70L, goalPercent(1750,2500)); assertEquals(0L,goalPercent(0,2500)) }
    @Test fun unitConversion() { assertEquals(250,VolumeUnit.ML.toMl(250.0)); assertEquals(296,VolumeUnit.US_OZ.toMl(10.0)) }
    @Test fun midnightIsExclusive() {
        val zone = ZoneId.of("Europe/Ljubljana"); val date = LocalDate.of(2026,10,31); val window = dayWindow(date,zone)
        assertEquals(date,localDay(window.start,zone)); assertEquals(date,localDay(window.endExclusive-1,zone)); assertEquals(date.plusDays(1),localDay(window.endExclusive,zone))
    }
    @Test fun springDayHas23Hours() { val w = dayWindow(LocalDate.of(2026,3,29),ZoneId.of("Europe/Ljubljana")); assertEquals(23L*3600000,w.endExclusive-w.start) }
    @Test fun autumnDayHas25Hours() { val w = dayWindow(LocalDate.of(2026,10,25),ZoneId.of("Europe/Ljubljana")); assertEquals(25L*3600000,w.endExclusive-w.start) }
    @Test fun timezoneChangesLocalDate() { val instant = Instant.parse("2026-10-31T23:30:00Z").toEpochMilli(); assertEquals(LocalDate.of(2026,11,1),localDay(instant,ZoneId.of("Europe/Ljubljana"))); assertEquals(LocalDate.of(2026,10,31),localDay(instant,ZoneId.of("America/New_York"))) }
    @Test fun allCategoriesAvailable() { assertEquals(12,DrinkCategory.entries.size) }
    @Test(expected = IllegalArgumentException::class) fun rejectZeroVolume() { validateVolume(0) }
}

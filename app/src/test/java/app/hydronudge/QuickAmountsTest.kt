package app.hydronudge

import app.hydronudge.domain.*
import org.junit.Assert.*
import org.junit.Test

class QuickAmountsTest {
    @Test fun parsesExactlyFourCompleteValidAmounts() {assertEquals(listOf(150,250,330,500),parseQuickAmounts("150, 250, 330, 500",VolumeUnit.ML))}
    @Test fun malformedTokensCannotDisappear() {
        listOf("150,250,invalid,330,500","150,,250,330,500","150,250,330,500,","150,250,nope,500","150,250,330").forEach {assertNull(it,parseQuickAmounts(it,VolumeUnit.ML))}
    }
    @Test fun rejectsNonfiniteNegativeZeroOverflowAndRoundedZero() {
        listOf("NaN,250,330,500","Infinity,250,330,500","0,250,330,500","-1,250,330,500","20001,250,330,500","0.01,250,330,500").forEach {assertNull(it,parseQuickAmounts(it,VolumeUnit.ML))}
    }
    @Test fun convertsFluidOuncesOnlyAfterAllTokensAreValidated() {assertEquals(listOf(148,237,296,473),parseQuickAmounts("5,8,10,16",VolumeUnit.US_OZ))}
}

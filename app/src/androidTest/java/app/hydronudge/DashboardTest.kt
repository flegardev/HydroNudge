package app.hydronudge

import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.*
import org.junit.Rule
import org.junit.Test

class DashboardTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()
    @Test fun dashboardAndDrinkChooserAreAccessible() {
        compose.waitUntil(15000) { compose.onAllNodesWithText("Choose a drink").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithText("Choose a drink").performScrollTo().performClick()
        compose.onNodeWithText("Make it a sip").assertIsDisplayed()
        compose.onNodeWithText("Milk").assertExists()
        compose.onNodeWithText("Protein shake").assertExists()
    }
}

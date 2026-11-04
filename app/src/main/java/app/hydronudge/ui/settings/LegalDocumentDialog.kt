package app.hydronudge.ui.settings

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/** Offline notices remain accessible without a browser or network permission. */
@Composable
internal fun LegalDocumentDialog(title: String, asset: String, close: () -> Unit) {
    val context = LocalContext.current
    val text = remember(asset) {
        context.assets.open(asset).bufferedReader(Charsets.UTF_8).use { it.readText() }
    }
    AlertDialog(
        onDismissRequest = close,
        title = { Text(title) },
        text = {
            SelectionContainer {
                Text(text, Modifier.heightIn(max = 460.dp).verticalScroll(rememberScrollState()))
            }
        },
        confirmButton = { TextButton(onClick = close) { Text("Close") } }
    )
}

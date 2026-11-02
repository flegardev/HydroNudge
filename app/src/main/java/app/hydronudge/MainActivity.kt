package app.hydronudge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import app.hydronudge.ui.HydroApp
import app.hydronudge.ui.HydroViewModel
import app.hydronudge.ui.theme.HydroTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onResume() {
        super.onResume()
        (application as HydroApplication).let { app -> app.applicationScope.launch { app.refreshSurfaces() } }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val model: HydroViewModel = viewModel()
            val state by model.state.collectAsStateWithLifecycle()
            HydroTheme(state.settings) { HydroApp(state, model) }
        }
    }
}

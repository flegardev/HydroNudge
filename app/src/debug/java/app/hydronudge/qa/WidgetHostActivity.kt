package app.hydronudge.qa

import android.app.Activity
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import app.hydronudge.widgets.HydroWidgetReceiver

/** Debug-only native host for exact widget size QA; never included in release. */
class WidgetHostActivity : Activity() {
    private lateinit var host: AppWidgetHost
    private var widgetId = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        check(Build.MODEL.contains("sdk", true)) { "Widget QA host is emulator-only" }
        val width = intent.getIntExtra("width",240)
        val height = intent.getIntExtra("height",150)
        val manager = AppWidgetManager.getInstance(this)
        host = AppWidgetHost(this, 21702)
        host.deleteHost() // Only this debug-only host; recover IDs from an interrupted QA run.
        widgetId = host.allocateAppWidgetId()
        val provider = ComponentName(this,HydroWidgetReceiver::class.java)
        val options = Bundle().apply {
            putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH,width)
            putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH,width)
            putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT,height)
            putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT,height)
        }
        check(manager.bindAppWidgetIdIfAllowed(widgetId,provider,options)) { "Grant widget binding to this emulator debug app before QA" }
        host.startListening()
        val view = host.createView(this,widgetId,manager.getAppWidgetInfo(widgetId))
        view.setPadding(0,0,0,0)
        view.updateAppWidgetSize(options,width,height,width,height)
        val density = resources.displayMetrics.density
        val root = FrameLayout(this).apply { setBackgroundColor(android.graphics.Color.rgb(7,21,34)) }
        root.addView(view,FrameLayout.LayoutParams((width*density).toInt(),(height*density).toInt(),Gravity.CENTER))
        setContentView(root)
    }
    override fun onDestroy() {
        if(::host.isInitialized) {
            if(widgetId >= 0) host.deleteAppWidgetId(widgetId)
            host.stopListening()
        }
        super.onDestroy()
    }
}

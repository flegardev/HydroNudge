package app.hydronudge.qa

import android.app.Activity
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/** Captures only the tested app window, never other applications or the notification shade. */
object QaArtifacts {
    private val instrumentation get()=InstrumentationRegistry.getInstrumentation()
    val folder: File get()=File(instrumentation.targetContext.getExternalFilesDir(null),"qa-reports").apply {mkdirs()}
    fun note(message: String) { File(folder,"device-results.txt").appendText("${java.time.Instant.now()} $message\n") }
    fun screenshot(activity: Activity,name: String) {
        lateinit var bitmap: Bitmap
        instrumentation.runOnMainSync { bitmap=Bitmap.createBitmap(activity.window.decorView.width,activity.window.decorView.height,Bitmap.Config.ARGB_8888) }
        val latch=CountDownLatch(1)
        var result=-1
        instrumentation.runOnMainSync { PixelCopy.request(activity.window,bitmap,{result=it;latch.countDown()},Handler(Looper.getMainLooper())) }
        check(latch.await(10,TimeUnit.SECONDS) && result==PixelCopy.SUCCESS) {"App-window screenshot failed ($result)"}
        File(folder,"$name.png").outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG,100,it) }
        bitmap.recycle()
        note("SCREENSHOT $name.png (app window)")
    }
    fun logcat(name: String) {
        val pid=android.os.Process.myPid()
        val process=ProcessBuilder("logcat","-d","--pid=$pid","-t","1000","-v","threadtime").redirectErrorStream(true).start()
        File(folder,"$name-logcat.txt").outputStream().use {output -> process.inputStream.use {it.copyTo(output)} }
        process.waitFor(10,TimeUnit.SECONDS)
    }
    fun shell(command: String): String = android.os.ParcelFileDescriptor.AutoCloseInputStream(instrumentation.uiAutomation.executeShellCommand(command)).bufferedReader().use {it.readText()}
}

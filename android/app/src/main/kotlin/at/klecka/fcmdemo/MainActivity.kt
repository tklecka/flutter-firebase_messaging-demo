package at.klecka.fcmdemo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant

class MainActivity : FlutterActivity() {
    private val CHANNEL = "at.klecka.fcmdemo/info"
    val infochannelId = "info_channel"
    val infochannelName = "Info Channel"

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
            // Note: this method is invoked on the main thread.
            call, result ->
            if (call.method == "setnotificationManager") {
                val notificationManager = setnotificationManager()

                if (notificationManager.isNotEmpty()) {
                    result.success(notificationManager)
                } else {
                    result.error("UNAVAILABLE", "Error on setnotificationManager!", null)
                }
            } else {
                result.notImplemented()
            }
        }
        GeneratedPluginRegistrant.registerWith(flutterEngine)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setInfoChannel() {
        val notificationManager = getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(infochannelId, infochannelName, NotificationManager.IMPORTANCE_DEFAULT)
        channel.setBypassDnd(false)
        channel.enableVibration(true)
        notificationManager.createNotificationChannel(channel)
    }

    private fun setnotificationManager(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setInfoChannel()
            val notificationManager = getSystemService(NotificationManager::class.java)
            return notificationManager.notificationChannels.toString()
        }
        return Build.VERSION.SDK_INT.toString();
    }
}
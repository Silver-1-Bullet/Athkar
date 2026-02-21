package com.istighfar.counter.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.glance.appwidget.updateAll
import com.istighfar.counter.data.CounterRepository
import com.istighfar.counter.widget.IstighfarWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

class MidnightResetReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = CounterRepository(context)
                if (repo.isAutoResetEnabled()) {
                    repo.resetTodayCount()
                    IstighfarWidget().updateAll(context)
                }
                scheduleMidnightAlarm(context)
            } finally {
                pendingResult.finish()
            }
        }
    }

    companion object {
        private const val TAG = "MidnightResetReceiver"
        private const val REQUEST_CODE = 1001

        fun scheduleMidnightAlarm(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, MidnightResetReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val triggerMillis = LocalDate.now().plusDays(1)
                .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

            val canScheduleExact = Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
                    alarmManager.canScheduleExactAlarms()

            if (canScheduleExact) {
                try {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerMillis,
                        pendingIntent
                    )
                    return
                } catch (e: SecurityException) {
                    Log.e(TAG, "Failed to schedule exact alarm, falling back to inexact alarm", e)
                }
            } else {
                Log.w(TAG, "Exact alarm permission not granted, using inexact alarm as fallback")
            }

            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerMillis,
                pendingIntent
            )
        }
    }
}

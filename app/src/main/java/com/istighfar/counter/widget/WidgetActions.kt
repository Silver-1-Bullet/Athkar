package com.istighfar.counter.widget

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import com.istighfar.counter.data.CounterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private suspend fun syncWidgetState(context: Context, glanceId: GlanceId) {
    val snapshot = CounterRepository(context).getSnapshot()
    updateAppWidgetState(context, glanceId) { prefs ->
        prefs.toMutablePreferences().apply {
            this[IstighfarWidget.todayCountKey] = snapshot.first
            this[IstighfarWidget.lifetimeCountKey] = snapshot.second
            this[IstighfarWidget.dhikrKey] = snapshot.third
        }
    }
    IstighfarWidget().update(context, glanceId)
}

class IncrementAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        // 1. تقديم اهتزاز فوري للمستخدم ليشعر بالاستجابة
        hapticFeedback(context)

        // 2. تحديث البيانات في الخلفية
        withContext(Dispatchers.IO) {
            CounterRepository(context).incrementCount()
        }

        // 3. تحديث حالة الـ Widget وواجهته مباشرة
        syncWidgetState(context, glanceId)
    }

    private fun hapticFeedback(context: Context) {
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // استخدام تأثير الضغطة الخفيفة العصرية
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(30)
            }
        } catch (_: Exception) {
            // الاهتزاز غير مدعوم أو غير مسموح
        }
    }
}

class ResetTodayAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        withContext(Dispatchers.IO) {
            CounterRepository(context).resetTodayCount()
        }

        // تحديث حالة الـ Widget وواجهته مباشرة
        syncWidgetState(context, glanceId)
    }
}

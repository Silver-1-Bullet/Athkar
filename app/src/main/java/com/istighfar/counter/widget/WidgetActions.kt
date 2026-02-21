package com.istighfar.counter.widget

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.updateAll
import com.istighfar.counter.data.CounterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

        // 3. تحديث واجهة الـ Widget
        IstighfarWidget().updateAll(context)
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
        IstighfarWidget().updateAll(context)
    }
}

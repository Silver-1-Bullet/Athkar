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
        withContext(Dispatchers.IO) {
            CounterRepository(context).incrementCount()
        }
        hapticFeedback(context)
        IstighfarWidget().updateAll(context)
    }

    private fun hapticFeedback(context: Context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                val vibrator = vibratorManager.defaultVibrator
                vibrator.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
            }
        } catch (_: Exception) {
            // Vibration not available
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

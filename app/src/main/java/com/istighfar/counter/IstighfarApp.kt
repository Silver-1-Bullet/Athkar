package com.istighfar.counter

import android.app.Application
import com.istighfar.counter.receiver.MidnightResetReceiver

class IstighfarApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MidnightResetReceiver.scheduleMidnightAlarm(this)
    }
}

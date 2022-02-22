package com.bear.arcdemo.source.render

import android.view.Choreographer
import com.bear.arcdemo.arc.data.bearLog
import java.util.*
import kotlin.concurrent.timerTask

class FpsMonitor private constructor() {
    private var mCountPerSecond = 0

    private var mTimer: Timer? = null


    private val mCallBack = Choreographer.FrameCallback {
        mCountPerSecond++
    }

    private fun startTimer() {
        mTimer?.cancel()
        mTimer = Timer()
        mTimer!!.schedule(timerTask {
            bearLog("Fps:<$mCountPerSecond>")
            mCountPerSecond = 0
        }, 0L, 1000L)

    }


    fun register() {
        Choreographer.getInstance().postFrameCallback(mCallBack)
        startTimer()
    }

    fun unRegister() {
        Choreographer.getInstance().removeFrameCallback(mCallBack)
        mTimer?.cancel()
    }


    companion object {
        val instance: FpsMonitor by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            FpsMonitor()
        }
    }
}
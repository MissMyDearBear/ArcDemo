package com.bear.arcdemo.showcode

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.os.Message
import java.lang.ref.WeakReference

class HandlerCode {
}

/**
 * 解决Handler内部类持有外部类（Activity）引用，从而可能出现内存泄漏问题
 *
 *
 */
class WeakHandler(activity: Activity) {
    private val weakActivity: WeakReference<Activity> = WeakReference(activity)
    var mHandler: Handler? = null
    private fun ensureHandler(): Boolean {
        if (weakActivity.get() == null || true == weakActivity.get()?.isFinishing) {
            return false
        }
        if (mHandler == null) {
            mHandler = Handler(weakActivity.get()!!.mainLooper)
        }
        return true
    }


    fun post(r: Runnable): Boolean? {
        if (!ensureHandler()) {
            return false

        }
        return mHandler?.post(r)
    }
    //
}

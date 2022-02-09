package com.bear.arcdemo.source.multhread

import java.util.concurrent.CountDownLatch

class ThreadAwait(runnable: Runnable, name: String, private val ctl: CountDownLatch?) :
    Thread(runnable, name) {
    override fun run() {
        sleep(100)
        super.run()
        ctl?.countDown()
    }
}
package com.bear.arcdemo.source.multhread

import com.bear.arcdemo.bearLog
import java.util.concurrent.atomic.AtomicInteger

class MulThread(name: String) : Thread(name) {

    override fun run() {
        super.run()
        synchronized(MulThread.javaClass){
            sleep(50)
//            autoCount.incrementAndGet()
            count++
            bearLog("count<$count>,atoCount<${autoCount.get()}>")
        }

    }


    companion object {
        var count: Int = 0
        val autoCount: AtomicInteger = AtomicInteger(0)//CAS
    }
}
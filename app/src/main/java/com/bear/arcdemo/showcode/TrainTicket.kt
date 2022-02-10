package com.bear.arcdemo.showcode

import com.bear.arcdemo.bearLog
import java.util.concurrent.atomic.AtomicInteger

/**
 * 卖火车票
 */
class TrainTicket() {

    fun sell() {
        if (count.get() == 0) {
            bearLog("the ticket sell out!")
            return
        }
        var tem = count.decrementAndGet()
        bearLog("left count-> <$tem>")
    }

    companion object {
        val count: AtomicInteger = AtomicInteger(100)
    }
}
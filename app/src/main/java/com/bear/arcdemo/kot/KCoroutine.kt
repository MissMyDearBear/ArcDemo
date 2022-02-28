package com.bear.arcdemo.kot

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class KCoroutine {

    fun main() = runBlocking {
        fut1()
        print("Hello,")

    }

    private suspend fun fut1() {
        delay(1000)
        print("world!")
    }
}
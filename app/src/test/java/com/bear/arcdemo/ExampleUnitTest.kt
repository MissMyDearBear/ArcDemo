package com.bear.arcdemo

import com.bear.arcdemo.kot.KCoroutine
import kotlinx.coroutines.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testCoroutineMain() {
        KCoroutine().main()
    }

    @Test
    fun testCoroutine() = runBlocking {
        launch {
            delay(200L)
            println("Task from runBlocking")
        }

        coroutineScope { // 创建一个协程作用域
            launch {
                delay(500L)
                println("Task from nested launch")
            }

            delay(100L)
            println("Task from coroutine scope") // 这一行会在内嵌 launch 之前输出
        }

        println("Coroutine scope is over") // 这一行在内嵌 launch 执行完毕后才输出
    }

    @Test
    fun testSum() = runBlocking {
        val one = async { actionOne() }
        val two = async { actionTwo() }
        print("result = ${one.await() + two.await()} \n")
    }

    suspend fun actionOne(): Int {
        delay(1000)
        print("first job start\n")
        return 13
    }

    suspend fun actionTwo(): Int {
        delay(1000)
        print("second job start\n")
        return 29
    }

}
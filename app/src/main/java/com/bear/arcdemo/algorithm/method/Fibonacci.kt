package com.bear.arcdemo.algorithm.method

class Fibonacci {

    fun fabonacci(n: Int): Int {
        if (n < 2) {
            return 1
        }
        return fabonacci(n - 1) + fabonacci(n - 2)
    }
}
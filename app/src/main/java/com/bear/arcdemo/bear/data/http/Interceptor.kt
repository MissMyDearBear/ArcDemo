package com.bear.arcdemo.bear.data.http

interface Interceptor<T> {
    fun intercept(chain: Chain<T>): Result<out T>
    interface Chain<T> {
        fun request(): Request
        fun process(result: Result<out T>): Request
    }
}


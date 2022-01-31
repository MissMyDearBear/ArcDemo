package com.bear.arcdemo.arc.data.http

import java.util.*

class RealCallChain(
    private val interceptors: LinkedList<Interceptor<String>>,
    private val index: Int,
    private val request: Request
) : Interceptor.Chain<String> {
    override fun request(): Request {
        return request
    }

    override fun process(result: Result<String>): Request {
        TODO("Not yet implemented")
    }
}
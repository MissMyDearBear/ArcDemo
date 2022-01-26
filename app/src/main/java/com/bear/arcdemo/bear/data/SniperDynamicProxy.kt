package com.bear.arcdemo.bear.data

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy


class SniperDynamicProxy(private var mObject: Any) : InvocationHandler {

    fun newProxyInstance(): Any {
        return Proxy.newProxyInstance(
            mObject.javaClass.classLoader,
            mObject.javaClass.interfaces,
            this
        )
    }

    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        return method?.invoke(mObject, *(args ?: arrayOfNulls<Any>(0)))
    }


}
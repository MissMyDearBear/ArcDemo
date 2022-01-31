package com.bear.arcdemo.arc.data

import com.bear.arcdemo.arc.data.api.PaySniper
import com.bear.arcdemo.arc.data.model.PayInfo

class PayDataSource(private val paySniper: PaySniper) {
    private val proxy by lazy {
        SniperDynamicProxy(paySniper).newProxyInstance() as PaySniper
    }

    fun toPay(amount: Int): Result<PayInfo> {
        val result = proxy.toPay(amount)
        if (result.isNotEmpty()) {
            return Result.Success(PayInfo())
        }
        return Result.Error(Exception("something wrong!!"))
    }
}
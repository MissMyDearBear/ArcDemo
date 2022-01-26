package com.bear.arcdemo.bear.data

import com.bear.arcdemo.bear.data.api.PaySniper
import com.bear.arcdemo.bear.data.model.PayInfo
import com.bear.arcdemo.data.Result

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
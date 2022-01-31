package com.bear.arcdemo.arc.data.api

import com.bear.arcdemo.arc.data.HttpUtils

class PaySniperImpl : PaySniper {
    private val url = "http://testPay"
    override fun toPay(amount: Int): String {
        val postUrl = "$url?amount$amount"
        return HttpUtils.instance.post(postUrl);
    }

    override fun backMoney(): String {
        TODO("Not yet implemented")
    }
}
package com.bear.arcdemo.arc.data.api

interface PaySniper {
    //支付
    fun toPay(amount: Int): String

    //退款
    fun backMoney(): String
}
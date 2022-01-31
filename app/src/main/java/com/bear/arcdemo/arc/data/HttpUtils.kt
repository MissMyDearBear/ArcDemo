package com.bear.arcdemo.arc.data

class HttpUtils private constructor() {

    fun post(url: String): String {
        //to
        Thread.sleep(200)
        return "支付成功"
    }


    companion object {
        val instance: HttpUtils by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            HttpUtils()
        }
    }
}
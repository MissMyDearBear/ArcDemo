package com.bear.arcdemo.arc.data

import com.bear.arcdemo.arc.data.model.PayInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PayRepository(private val payDataSource: PayDataSource) {

    var payInfo: PayInfo? = null
        private set

    init {
        payInfo = null
    }

    suspend fun toPay(amount: Int): Result<PayInfo> {
        return withContext(Dispatchers.IO) {
            val result = payDataSource.toPay(amount)
            if (result is Result.Success) {
                updatePayInfo(result.data)
            }
            result
        }

    }

    private fun updatePayInfo(payInfo: PayInfo) {
        this.payInfo = payInfo
    }
}
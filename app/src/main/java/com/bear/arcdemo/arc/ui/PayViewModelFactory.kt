package com.bear.arcdemo.arc.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bear.arcdemo.arc.data.PayDataSource
import com.bear.arcdemo.arc.data.PayRepository
import com.bear.arcdemo.arc.data.api.PaySniperImpl

class PayViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PayViewModel::class.java)) {
            return PayViewModel(
                payRepository = PayRepository(
                    payDataSource = PayDataSource(
                        paySniper = PaySniperImpl()
                    )
                )
            ) as T
        }
        throw IllegalArgumentException("unKnown view model")
    }
}
package com.bear.arcdemo.arc.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bear.arcdemo.arc.data.PayRepository
import com.bear.arcdemo.arc.data.model.PayInfo
import com.bear.arcdemo.algorithm.sort.QuickSort
import com.bear.arcdemo.algorithm.sort.Sort
import com.bear.arcdemo.algorithm.sort.SortDynamicProxy
import com.bear.arcdemo.arc.data.Result
import kotlinx.coroutines.launch

class PayViewModel(private val payRepository: PayRepository) : ViewModel() {

    private val _payResult = MutableLiveData<PayResult>()
    val payResult: MutableLiveData<PayResult> = _payResult

    fun toPay(amount: Int) {
        viewModelScope.launch {
            when (payRepository.toPay(amount)) {
                is Result.Success<PayInfo> -> _payResult.value =
                    PayResult(success = PayForUser("支付成功"))
                else -> _payResult.value = PayResult(errorCode = -1)
            }
        }

    }

    fun sort(array: Array<Int>): Array<Int> {
//        val sort = SortDynamicProxy(BubbleSort()).newProxyInstance() as Sort
        val sort = SortDynamicProxy(QuickSort()).newProxyInstance() as Sort
        return sort.sort(array)
    }

}
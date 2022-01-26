package com.bear.arcdemo.bear.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bear.arcdemo.R
import com.bear.arcdemo.bear.data.bearLog
import com.bear.arcdemo.databinding.PayActivityBinding

class PayActivity : AppCompatActivity() {
    private lateinit var binding: PayActivityBinding

    private val viewModel by lazy {
        ViewModelProvider(this, PayViewModelFactory())[PayViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bearLog("onCreate")
        binding = DataBindingUtil.setContentView(this, R.layout.pay_activity)
        viewModel.payResult.observe(
            this,
            Observer {
                binding.payResult = it.success
            }
        )
        binding.mBtn.setOnClickListener {
            val array = arrayOf<Int>(1,4,7,90,2,34,55,664,2,34,5,67)
            val result=viewModel.sort(array)
            bearLog(result.toString())
        }

    }

    private fun doSomething() {
        Thread.sleep(500)
    }

    override fun onStart() {
        super.onStart()
        bearLog("onStart")
    }

    override fun onResume() {
        super.onResume()
        bearLog("onResume")
    }

    override fun onRetainCustomNonConfigurationInstance(): Any? {
        bearLog("onRetainCustomNonConfigurationInstance")
        return super.onRetainCustomNonConfigurationInstance()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        bearLog("onWindowFocusChanged")
        doSomething()
    }
}
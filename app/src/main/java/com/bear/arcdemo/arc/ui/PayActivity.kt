package com.bear.arcdemo.arc.ui

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bear.arcdemo.R
import com.bear.arcdemo.arc.data.bearLog
import com.bear.arcdemo.databinding.PayActivityBinding
import com.bear.arcdemo.ndk.NLog

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
        binding.mBtn.text = "Action"
        binding.mBtn.setOnClickListener {
//            val array = arrayOf<Int>(1,4,7,90,2,34,55,664,2,34,5,67)
//            val result=viewModel.sort(array)
//            bearLog(result.toString())
//            viewModel.backTrace()
//            viewModel.subsets()
//            viewModel.mulThread()
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                viewModel.download()
//            }
            NLog.nBearLog("bear press jni log")
//            viewModel.fraction()
//            viewModel.buyTicket()
//            viewModel.kotOperation()
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
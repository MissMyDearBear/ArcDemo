package com.bear.arcdemo.arc.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bear.arcdemo.R
import com.bear.arcdemo.arc.data.bearLog
import com.bear.arcdemo.arc.service.DownloadService
import com.bear.arcdemo.databinding.PayActivityBinding
import com.bear.arcdemo.source.render.FpsMonitor

class PayActivity : AppCompatActivity() {
    private lateinit var binding: PayActivityBinding

    private val viewModel by lazy {
        ViewModelProvider(this, PayViewModelFactory())[PayViewModel::class.java]
    }

    private val myConnection = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName?,
            service: IBinder?
        ) {
            if (service is DownloadService.DownloadBinder) {
                val mService = service.getService()
                mService.setListener(object : DownloadService.OnProgressListener {
                    override fun onProgress(progress: Int) {
                        bearLog("progress/total -> $progress/16,curThread <${Thread.currentThread()}>")
                    }

                })
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            TODO("Not yet implemented")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bearLog("onCreate")
//        FpsMonitor.instance.register()
        binding = DataBindingUtil.setContentView(this, R.layout.pay_activity)
        viewModel.payResult.observe(
            this,
            Observer {
                binding.payResult = it.success
            }
        )
        binding.mBtn.text = "Action"
        val mainThread = Thread.currentThread();
        binding.mBtn.setOnClickListener {
            val array = intArrayOf(1,4,7,90,2,34,55,664,2,34,5,67)
            val result=viewModel.sort(array)
            bearLog(result.toString())
//            viewModel.backTrace()
//            viewModel.subsets()
//            viewModel.mulThread()
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
////                viewModel.download()
//                Intent(this, DownloadService::class.java).let {
//                    startService(it)
//                    bindService(it, myConnection, Context.BIND_AUTO_CREATE)
//                }
//            }
//            NLog.nBearLog("bear press jni log")
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

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        unbindService(myConnection)
        FpsMonitor.instance.unRegister()
        super.onDestroy()
    }
}
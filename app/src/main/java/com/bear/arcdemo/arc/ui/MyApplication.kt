package com.bear.arcdemo.arc.ui

import android.app.Application

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        myApplication = this
    }
    companion object {
        var myApplication: MyApplication ?= null
    }
}
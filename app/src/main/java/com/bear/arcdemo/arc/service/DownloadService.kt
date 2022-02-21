package com.bear.arcdemo.arc.service

import android.app.IntentService
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.Build
import android.os.IBinder
import com.bear.arcdemo.bearLog
import com.bear.arcdemo.showcode.DownLoadManager
import com.bear.arcdemo.showcode.DownloadTask
import java.util.concurrent.atomic.AtomicInteger

class DownloadService : IntentService("downloadService") {
    private var startId = 0;

    private var progress: AtomicInteger = AtomicInteger(0)

    private var listener: OnProgressListener? = null
    fun setListener(l: OnProgressListener) {
        listener = l
    }

    override fun onHandleIntent(intent: Intent?) {
        for (i in 0..15) {
            var report = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                DownLoadManager.instance.addTask(
                    DownloadTask(
                        "https://hbimg.huabanimg.com/e7aed7a6b8cb9f561d212176fd2094742e006938124ca-1lFwAm_fw658/format/webp",
                        "pic${i}"
                    ),
                    onProgress = {
                        if (!report && it == 100) {
                            progress.incrementAndGet().let { pro ->
                                listener?.onProgress(pro)
                                report = true
                            }
                        }
                    }
                )
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        bearLog("$this service onCreate")
    }

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
        this.startId = startId
        bearLog("<$this >service onStart")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        bearLog("<$this >service onStartCommand,flag<$flags>,startId<$startId>")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        bearLog("$this service onDestroy")
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        bearLog("$this service onBind")
        return DownloadBinder()
    }

    inner class DownloadBinder : Binder() {
        fun getService(): DownloadService {
            return this@DownloadService
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        bearLog("$this service onUnbind")
        return super.onUnbind(intent)
    }

    override fun unbindService(conn: ServiceConnection) {
        super.unbindService(conn)
        bearLog("$this service unbindService")
    }

    interface OnProgressListener {
        fun onProgress(progress: Int)
    }
}
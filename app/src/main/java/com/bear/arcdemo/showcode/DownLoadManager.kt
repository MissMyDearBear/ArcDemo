package com.bear.arcdemo.showcode

import android.os.Build
import androidx.annotation.RequiresApi
import com.bear.arcdemo.BuildConfig
import com.bear.arcdemo.arc.ui.MyApplication
import com.bear.arcdemo.bearLog
import java.util.*
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread

class DownLoadManager private constructor() {


    private val executorService: ExecutorService = ThreadPoolExecutor(
        BuildConfig.DOWNLOAD_MAX_COUNT, Integer.MAX_VALUE,
        60L, TimeUnit.SECONDS,
        SynchronousQueue<Runnable>()
    )


    private val taskCache: CopyOnWriteArrayList<String> = CopyOnWriteArrayList()


    @RequiresApi(Build.VERSION_CODES.N)
    fun addTask(task: DownloadTask) {
        //不能重复添加task
        if (taskCache.contains(task.url)) {
            return
        }
        executorService.execute {
            bearLog("thread#<${Thread.currentThread().name}>")
            try {
                PictureDownload.instance.download(
                    task.url,
                    task.name,
                    MyApplication.myApplication!!.applicationContext
                )
            } catch (e: Exception) {

            } finally {
                taskCache.remove(task.url)
            }

        }
    }


    companion object {
        val instance: DownLoadManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            DownLoadManager()
        }
    }

}

enum class DownLoadState {
    initState,//初始状态
    running,//运行中
    complete,//完成
    cancel,//取消
    error//异常
}

data class DownloadTask(
    val url: String,
    val name: String,
)

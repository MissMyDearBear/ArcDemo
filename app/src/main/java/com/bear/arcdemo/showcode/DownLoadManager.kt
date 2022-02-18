package com.bear.arcdemo.showcode

import android.os.Build
import androidx.annotation.RequiresApi
import com.bear.arcdemo.arc.ui.MyApplication
import com.bear.arcdemo.bearLog
import java.util.concurrent.*
import kotlin.math.min

class DownLoadManager private constructor() {
    private val cpuCount = Runtime.getRuntime().availableProcessors()
    private val coreCount = 2.coerceAtLeast(min(cpuCount - 1, 4))

    private val executorService: ExecutorService = ThreadPoolExecutor(
        coreCount, coreCount * 2,
        60L, TimeUnit.SECONDS,
        LinkedBlockingQueue(),
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
                bearLog(e.toString())
            } finally {
                taskCache.remove(task.url)
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun addTask(task: DownloadTask, onProgress: (Int) -> Unit) {
        //不能重复添加task
        if (taskCache.contains(task.url)) {
            return
        }
        executorService.execute {
            bearLog("thread#<${Thread.currentThread().name}>")
            try {
                PictureDownload.instance.downloadWithProgress(
                    task.url,
                    task.name,
                    MyApplication.myApplication!!.applicationContext,
                    onProgress
                )
            } catch (e: Exception) {
                print(e)
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

package com.bear.arcdemo.showcode

import com.bear.arcdemo.BuildConfig
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class DownLoadManager private constructor() {
    private val coreCount: Int by lazy {
        BuildConfig.DOWNLOAD_MAX_COUNT
    }

    //可执行task
    private val coreList: LinkedList<DownloadTask> = LinkedList<DownloadTask>()

    //等待队列
    private val waitList: LinkedList<DownloadTask> = LinkedList<DownloadTask>()

    //当前任务队列中task
    private val curTaskCount: AtomicInteger = AtomicInteger(0)


    public fun addTask(task: DownloadTask) {

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
    val savePath: String,
    val state: Int,
)

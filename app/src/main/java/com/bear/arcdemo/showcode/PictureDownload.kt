package com.bear.arcdemo.showcode

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.*

const val testUrl =
    "http://uat-i0.hdslb.com/bfs/manga-static/cad5f1587043c31779288de2a91a31b3cdc7be88.jpg"

class PictureDownload private constructor() {
    val coreSize = 5
    val taskList = LinkedList<PictureTask>()

    @RequiresApi(Build.VERSION_CODES.N)
    fun download(url: String, context: Context) {
        val url = URL(url)
        val http = url.openConnection()
        http.apply {
            connectTimeout = 6000
            doInput = true
            useCaches = false
        }
        val inputStream = http.getInputStream()

        val bitmap = BitmapFactory.decodeStream(inputStream) ?: return
        inputStream.close()

        val path = context.dataDir.absolutePath + File.separator + "pic"
        val file = File(path)
        if (!file.exists()) {
            file.mkdir()
        }

        val fOut = FileOutputStream(path + File.separator + "pic01.png")
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
        fOut.flush()
        fOut.close()

    }

    companion object {
        val instance: PictureDownload by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            PictureDownload()
        }
    }

}

data class PictureTask(
    val url: String,
    val name: String
)
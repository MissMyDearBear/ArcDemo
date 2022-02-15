package com.bear.arcdemo.showcode

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import com.bear.arcdemo.bearLog
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.*

class PictureDownload private constructor() {
    @RequiresApi(Build.VERSION_CODES.N)
    fun download(url: String, name: String, context: Context) {
        bearLog("download task <$name> running!!")
        val url = URL(url)
        val http = url.openConnection()
        http.apply {
            connectTimeout = 6000
            doInput = true
            useCaches = false
        }
        val inputStream = http.getInputStream()
        val contentLength = http.contentLength
        bearLog("download task <$name> size<$contentLength>")
        val bitmap = BitmapFactory.decodeStream(inputStream) ?: return
        inputStream.close()

        val path = context.dataDir.absolutePath + File.separator + "pic"
        val file = File(path)
        if (!file.exists()) {
            file.mkdir()
        }

        val fOut = FileOutputStream(path + File.separator + "$name.png")
        //图片压缩
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
        bearLog("download task <$name> save file success!!")
        fOut.flush()
        fOut.close()

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun downloadWithProgress(
        url: String,
        name: String,
        context: Context,
        onProcess: (Int) -> Unit
    ) {
        bearLog("download task <$name> running!!")
        val url = URL(url)
        val http = url.openConnection()
        http.apply {
            connectTimeout = 6000
            useCaches = false
            doInput = true
        }
        val inputStream = http.getInputStream()

        val path = context.dataDir.absolutePath + File.separator + "pic"
        val file = File(path)
        if (!file.exists()) {
            file.mkdir()
        }
        val fos = FileOutputStream(path + File.separator + "$name.png")
        val byte = ByteArray(1024)
        var downloadSize = 0
        do {
            val read = inputStream.read(byte)
            if (read == -1) {
                break
            }
            fos.write(byte, 0, read)
            downloadSize += read
            onProcess(downloadSize)
            fos.flush()

        } while (true)
        inputStream.close()
        fos.close()

    }

    companion object {
        val instance: PictureDownload by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            PictureDownload()
        }
    }

}
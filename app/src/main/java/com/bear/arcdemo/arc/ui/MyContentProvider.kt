package com.bear.arcdemo.arc.ui

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.view.WindowManager
import com.bear.arcdemo.arc.data.bearLog
import com.bear.arcdemo.arc.data.globalScale

class MyContentProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        val windowManager: WindowManager =
            context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager?.let {
            val width = it.defaultDisplay.width
            globalScale = width / 375.0
            bearLog("oriWith-> <$width>, globalScale-> <$globalScale>")
        }
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }
}
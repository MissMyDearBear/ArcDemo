package com.bear.arcdemo.arc.data.hotfix

import android.annotation.SuppressLint
import android.content.Context
import dalvik.system.DexClassLoader
import dalvik.system.PathClassLoader
import java.io.File
import java.lang.reflect.Array


@SuppressLint("DiscouragedPrivateApi")
fun loadDex(context: Context) {
    val dex: File = context.getDir("dexPath", Context.MODE_PRIVATE)
    val optimizeDir: String = dex.absolutePath + File.separator + "opt_dex"
    val fOpt = File(optimizeDir)

    //创建一个dexClassLoader加载这个dex
    val dexClassLoader =
        DexClassLoader(dex.absolutePath, fOpt.absolutePath, null, context.classLoader)
    //系统的classloader
    val pathClassLoader: PathClassLoader = context.classLoader as PathClassLoader

    try {
        //1. 先获取dexClassLoader里面的DexPathList类型的pathList
        val mDexClassLoader = Class.forName("dalvik.system.BaseDexClassLoader")
        val mPathFiled = mDexClassLoader.getDeclaredField("pathList")
        mPathFiled.isAccessible = true
        val mPathListObject = mPathFiled.get(dexClassLoader)

        //2. 通过DexPathList拿到dexElement对象
        val mPathClazz = mPathListObject.javaClass
        val mElementField = mPathClazz.getDeclaredField("dexElements")
        mElementField.isAccessible = true
        val mElements = mElementField.get(mPathClazz)

        //3. 拿到应用程序使用的类加载器pathList
        val baseDexClassLoader = Class.forName("dalvik.system.BaseDexClassLoader")
        val pathListFiled = baseDexClassLoader.getDeclaredField("pathList")
        pathListFiled.isAccessible = true
        val pathListObject = pathListFiled.get(pathClassLoader)

        //4.拿到系统的elements
        val element = pathListObject.javaClass
        val dexElementFiled = element.getDeclaredField("dexElements")
        dexElementFiled.isAccessible = true
        val dexElementObject = dexElementFiled.get(pathListObject)

        //5. 创建一个Element[]类型的dexElements实例
        val singleElementClass = dexElementObject.javaClass.componentType
        val sysElementLen: Int = Array.getLength(dexElementObject)
        val mElementLen: Int = Array.getLength(mElements)
        val newLen = sysElementLen + mElementLen
        val newElementS = Array.newInstance(singleElementClass, newLen)

        //6. 将修复的dex插入到Elements前面
        for (i in 0 until newLen) {
            if (i < mElementLen) {
                Array.set(newElementS, i, Array.get(mElements, i))
            } else {
                Array.set(newElementS, i, Array.get(dexElementObject, i))
            }
        }

        //7. 将新组合的Elements设置给系统的PathList的dexElements中
        val field = pathListFiled.javaClass.getDeclaredField("dexElements")
        field.isAccessible = true
        field.set(pathListObject, newElementS)

    } catch (e: Exception) {
        print(e.toString())
    } finally {

    }


}
package com.bear.arcdemo.open.glide

import android.app.Activity
import androidx.fragment.app.Fragment
import com.bumptech.glide.manager.LifecycleListener

/**
 * 图片加载库
 * 抽象所有图片加载为request （url，本地，assert，R文件等）
 * 1. 根据生命周期管理请求
 * 2. 二级缓存
 * 3. 支持gif和webp格式图片
 * 4. 使用bitmapPool 可以使bitmap复用
 * 5. 主动释放bitmap减少系统回收压力
 */
class BGlide {
    fun main() {
        val person: Person =
            Person.Builder("Bear").setPersonAge(18).setPersonPhone(123456789).builder()
    }
}

class Person {
    var name: String = ""
    var age: Int = 0
    var phoneNumber: Int = 0

    private constructor (builder: Builder) {
        this.name = builder.name
        this.age = builder.age
        this.phoneNumber = builder.phoneNumber
    }

    class Builder(private val na: String) {
        var name = na
        var age: Int = 0
        var phoneNumber: Int = 0
        fun setPersonName(name: String): Builder {
            this.name = name
            return this
        }

        fun setPersonAge(age: Int): Builder {
            this.age = age
            return this
        }

        fun setPersonPhone(phone: Int): Builder {
            this.phoneNumber = phone
            return this
        }

        fun builder(): Person {
            return Person(this)
        }
    }
}

class RequestManager {
    var lifecycle: LifecycleListener? = null

    //每个Activity or Fragment都会创建一个manger 去管理请求
    fun createWithActivity(activity: Activity) {}
    fun createWithFragment(activity: Fragment) {}

    fun onStart() {}
    fun onStop() {}
    fun onDestory() {}
}

/**
 * 二级缓存
 * 基于Lru算法的内存缓存以及本地持久化缓存
 */
class Cache {

    inner class LruCache {}
    inner class MemoryCache {}
    inner class DeskCache {}
}

class Resource {
    /**
     * ActivitySource
     */
    inner class ActivityResource {}
}
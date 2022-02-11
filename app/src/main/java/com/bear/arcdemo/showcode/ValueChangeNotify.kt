package com.bear.arcdemo.showcode

import java.util.*

/**
 * 观察者模式
 */
class ValueChangeNotify<T>(initValue: T) {

    private val observerList: LinkedList<CallBack> = LinkedList()

    private var value = initValue

    fun setValue(v: T) {
        if (this.value == v) {
            return
        }
        this.value = v
        notifyChanged()
    }

    private fun notifyChanged() {
        if (observerList.isEmpty()) {
            return
        }
        observerList.forEach {
            it.doNext(value)
        }

    }

    fun addObserver(c: CallBack) {
        if (observerList.contains(c)) {
            return
        }
        observerList.add(c)
    }

    fun clean() {
        observerList.clear()
    }

    interface CallBack {
        fun doNext(data: Any?)
    }
}

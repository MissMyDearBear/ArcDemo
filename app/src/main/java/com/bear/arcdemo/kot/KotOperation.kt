package com.bear.arcdemo.kot

import com.bear.arcdemo.bearLog
import com.bear.arcdemo.source.handler.WeakHandler

/**
 * kotlin 高阶函数
 *
 * apply 和 also 都是返回object本身
 *
 *
 */
class KotOperation {
    var weakHandler: WeakHandler? = null


    fun main() {
        //let
        weakHandler?.let {
            it.post {
                bearLog("exc an runAble")
            }
        }
        val person = Person()
        person.let {
            it.name = "let name"
            it.age = 0
        }
        bearLog("person.let -> person<${person.name},${person.age}>")
        person.also {
            it.name = "nico"
            it.age = 10
        }
        bearLog("person.also -> person<${person.name},${person.age}>")

        person.apply {
            name = "AlenBear"
            age = 18
        }
        bearLog("person.apply -> person<${person.name},${person.age}>")

        with(person) {
            name = "with"
            age = 0
        }
        bearLog("with(person )-> person<${person.name},${person.age}>")

        person.run {
            name = "run name"
            age = 20
        }
        bearLog("person.run -> person<${person.name},${person.age}>")


    }
}

class Person {
    var name = ""
    var age = 0
}
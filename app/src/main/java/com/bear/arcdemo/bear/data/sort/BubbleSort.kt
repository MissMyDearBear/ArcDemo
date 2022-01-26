package com.bear.arcdemo.bear.data.sort

class BubbleSort : Sort {
    override fun sort(array: Array<Int>): Array<Int> {
        if (array.isEmpty()) {
            return array
        }
        val tem = array.clone()
        for (i in 1 until tem.size) {
            var isSorted = true
            for (j in 0 until tem.size - i) {
                if (tem[j] > tem[j + 1]) {
                    val num = tem[j]
                    tem[j] = tem[j + 1]
                    tem[j + 1] = num
                    isSorted = false
                }
            }
            if (isSorted) {
                break
            }
        }
        return tem
    }
}
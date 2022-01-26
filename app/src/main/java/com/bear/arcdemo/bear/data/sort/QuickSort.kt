package com.bear.arcdemo.bear.data.sort

class QuickSort : Sort {


    override fun sort(array: Array<Int>): Array<Int> {
        return sort(array, 0, array.size - 1)
    }

    private fun sort(array: Array<Int>, l: Int, r: Int): Array<Int> {
        if (l < r) {
            val index = findIndex(array, l, r)
            sort(array, l, index - 1)
            sort(array, index + 1, r)
        }
        return array
    }

    private fun findIndex(array: Array<Int>, l: Int, r: Int): Int {
        var p = l
        var index = p + 1;
        for (i in index..r) {
            if (array[i] < array[p]) {
                swap(array, index, i)
                index++
            }
        }
        swap(array, index - 1, p)
        return index - 1
    }

    private fun swap(array: Array<Int>, x: Int, y: Int) {
        val tem = array[x]
        array[x] = array[y]
        array[y] = tem
    }


}
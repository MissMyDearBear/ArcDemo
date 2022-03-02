package com.bear.arcdemo.algorithm.sort

class QuickSort : Sort {


    override fun sort(array: IntArray): IntArray {
        return sort(array, 0, array.size - 1)
    }

    private fun sort(array: IntArray, l: Int, r: Int): IntArray {
        if (l < r) {
            val index = findIndex(array, l, r)
            sort(array, l, index - 1)
            sort(array, index + 1, r)
        }
        return array
    }

    private fun findIndex(array: IntArray, l: Int, r: Int): Int {
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

    private fun swap(array: IntArray, x: Int, y: Int) {
        val tem = array[x]
        array[x] = array[y]
        array[y] = tem
    }


}
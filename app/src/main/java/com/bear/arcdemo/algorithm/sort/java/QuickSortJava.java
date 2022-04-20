package com.bear.arcdemo.algorithm.sort.java;

import androidx.annotation.NonNull;

import com.bear.arcdemo.algorithm.sort.Sort;

public class QuickSortJava implements Sort {

    @NonNull
    @Override
    public int[] sort(@NonNull int[] array) {
        if(array.length == 0 || array.length ==1){
            return array;
        }
        sort(array, 0, array.length - 1);
        return array;
    }

    private void sort(int[] a, int start, int end) {
        if (start >= end) {
            return;
        }
        int left = start;
        int right = end;
        int p = a[(start + end) / 2];
        while (left <= right) {

            while (left <= right && a[left] < p) {
                left++;
            }

            while (left <= right && a[right] > p) {
                right--;
            }

            if (left <= right) {
                swap(a, left, right);
                left++;
                right--;
            }
        }
        sort(a, start, right);
        sort(a, left, end);
    }


    private void swap(int[] a, int i, int j) {
        int tem = a[i];
        a[i] = a[j];
        a[j] = tem;
    }
}

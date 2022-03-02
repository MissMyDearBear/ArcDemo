package com.bear.arcdemo.algorithm.sort.java;

import androidx.annotation.NonNull;

import com.bear.arcdemo.algorithm.sort.Sort;

public class QuickSortJava implements Sort {

    @NonNull
    @Override
    public int[] sort(@NonNull int[] array) {
        sort(array, 0, array.length - 1);
        return array;
    }

    private void sort(int[] a, int l, int r) {
        if (l > r) {
            return;
        }
        int start = l;
        int end = r;
        int p = (l + r) / 2;
        while (l <= r) {

            while (l <= r && a[l] < a[p]) {
                l++;
            }

            while (l <= r && a[r] > a[p]) {
                r--;
            }

            if (l <= r) {
                swap(a, l, r);
                l++;
                r--;
            }
        }
        sort(a, start, r);
        sort(a, l, end);
    }


    private void swap(int[] a, int i, int j) {
        int tem = a[i];
        a[i] = a[j];
        a[j] = tem;
    }
}

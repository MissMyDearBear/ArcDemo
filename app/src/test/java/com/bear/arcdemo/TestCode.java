package com.bear.arcdemo;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;

public class TestCode {
    @Test
    public void test() {
        // 0 -1000 随机取10 个不同的数字，从小到大打印出来
        int[] array = new int[10];
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            array[i] = r.nextInt(1000);
        }
        quickSort(array, 0, 9);
        System.out.print("end");


    }

    private void quickSort(int[] a, int l, int r) {
        if (l > r) {
            return;
        }
        int start = l;
        int end = r;
        int p = a[(l + r) / 2];
        while (l <= r) {
            while (l <= r && a[l] < p) {
                l++;
            }
            while (l <= r && a[r] > p) {
                r--;
            }
            if (l <= r) {
                swap(a, l, r);
                l++;
                r--;
            }
        }
        quickSort(a, start, r);
        quickSort(a, l, end);
    }

    private void swap(int[] a, int l, int r) {
        int tem = a[l];
        a[l] = a[r];
        a[r] = tem;
    }
}



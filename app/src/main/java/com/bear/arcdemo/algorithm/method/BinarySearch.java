package com.bear.arcdemo.algorithm.method;

import java.util.Arrays;

/**
 * 二分查找法
 */
public class BinarySearch {

    public boolean binarySearch(int[] nums, int target) {
        int n = nums.length;
        if (n == 0) return false;
        Arrays.sort(nums);

        return search(nums, 0, n - 1, target);
    }

    private boolean search(int[] nums, int l, int r, int target) {
        int L = l;
        int R = r;
        while (L <= R) {
            int mid = (L + R) / 2;
            if (nums[mid] == target) {
                return true;
            }
            if (nums[mid] < target) {
                L = mid + 1;
            } else {
                R = mid - 1;
            }
        }
        return false;
    }
}

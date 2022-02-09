package com.bear.arcdemo.algorithm.method;

import static com.bear.arcdemo.LogKt.bearLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 回溯算法
 * 1. 选择
 * 2. 回溯
 * 3. 撤销
 */

public class BackTrack {

    private List<List<Integer>> list = new ArrayList<>();

    /**
     * 给定一个不含重复数字的数组 nums ，返回其 所有可能的全排列 。你可以 按任意顺序 返回答案。
     *
     * @param nums
     * @return
     */
    public List<List<Integer>> permute(int[] nums) {
        List<Integer> input = new LinkedList<Integer>();
        for (int i : nums) {
            input.add(i);
        }
//        backTrack(new ArrayList<>(), nums);
        backTrack(input, 0, nums.length);
        return list;
    }

    //路径减支
    private void backTrack(List<Integer> path, int[] nums) {
        if (path.size() == nums.length) {
            list.add(new ArrayList<>(path));
            logList(path);
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (path.contains(nums[i])) {
                continue;
            }
            path.add(nums[i]);
            backTrack(path, nums);
            path.remove(path.size() - 1);
        }

    }

    private void backTrack(List<Integer> outPut, int k, int n) {
        if (k == n) {
            logList(outPut);
            list.add(new ArrayList<>(outPut));
        }
        for (int i = k; i < n; i++) {
            Collections.swap(outPut, i, k);
            backTrack(outPut, k + 1, n);
            Collections.swap(outPut, i, k);
        }

    }

    private void logList(List<Integer> list) {
        StringBuilder sb = new StringBuilder();
        for (int s : list) {
            sb.append(s).append(",");
        }
        bearLog(sb.toString());
    }

    /**
     * 78.子集
     * 给你一个整数数组 nums ，数组中的元素 互不相同 。返回该数组所有可能的子集（幂集）。
     * <p>
     * 解集 不能 包含重复的子集。你可以按 任意顺序 返回解集。
     *
     * <p>
     * 方法一： 循环将元素放到已生成的子集中
     * 方法二： 回溯
     * <p/>
     */
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> ret = new ArrayList<>();
//        subsetsFunction1(ret,nums);
        //枚举
        for (int k = 0; k <= nums.length; k++) {
            subsetsFunction2(k, 0, new ArrayList<>(), ret, nums);
        }
        return ret;
    }

    private void subsetsFunction1(List<List<Integer>> ret, int[] nums) {
        ret.add(new ArrayList<>());
        for (int i = 0; i < nums.length; i++) {
            int size = ret.size();
            for (int j = 0; j < size; j++) {
                List<Integer> tem = new ArrayList<>();
                tem.addAll(ret.get(j));
                tem.add(nums[i]);
                ret.add(tem);
            }
        }

    }

    /**
     * 回溯
     *
     * @param k     子集长度
     * @param start 元素起始位置
     * @param ret
     * @param nums
     */
    private void subsetsFunction2(int k, int start, List<Integer> subList, List<List<Integer>> ret, int[] nums) {
        if (k == 0) {
            ret.add(new ArrayList<>(subList));
            return;
        }

        for (int i = start; i < nums.length; i++) {
            subList.add(nums[i]);
            subsetsFunction2(k - 1, i + 1, subList, ret, nums);
            subList.remove(subList.size() - 1);
        }

    }


    /**
     * 给你一个整数数组 nums ，其中可能包含重复元素，请你返回该数组所有可能的子集（幂集）。
     * <p>
     * 解集 不能 包含重复的子集。返回的解集中，子集可以按 任意顺序 排列。
     * <p>
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/subsets-ii
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     */
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> ret = new ArrayList<>();
        Arrays.sort(nums);
        subsetsWithDupFunction2(0, new ArrayList<>(), ret, nums);
        return ret;

    }

    /**
     * 回溯
     *
     * @param index 元素起始位置
     * @param ret
     * @param nums
     */
    private void subsetsWithDupFunction2(int index, List<Integer> subList, List<List<Integer>> ret, int[] nums) {
        if (index == nums.length) {
            ret.add(new ArrayList<>(subList));
            return;
        }
        subList.add(nums[index]);
        subsetsWithDupFunction2(index + 1, subList, ret, nums);
        subList.remove(subList.size() - 1);

        while (index<nums.length-1 && nums[index] == nums[index-1]){
            index++;
        }
        subsetsWithDupFunction2(index + 1, subList, ret, nums);

    }


}

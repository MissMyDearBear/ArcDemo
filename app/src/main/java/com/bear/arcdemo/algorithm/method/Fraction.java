package com.bear.arcdemo.algorithm.method;

import java.util.ArrayList;
import java.util.List;

/**
 * 1447. 最简分数
 * 给你一个整数 n ，请你返回所有 0 到 1 之间（不包括 0 和 1）满足分母小于等于  n 的 最简 分数 。分数可以以 任意 顺序返回。
 */
public class Fraction {

    public List<String> simplifiedFractions(int n) {
        if (n < 2) {
            return new ArrayList();
        }
        List<String> ret = new ArrayList();
        for (int i = 2; i <= n; i++) {
            for (int j = 1; j < i; j++) {
                if (isValid(j, i)) {
                    ret.add(j + "/" + i);
                }
            }
        }
        return ret;
    }

    private boolean isValid(int a, int b) {
        return gcd(a, b) == 1;
    }

    /**
     * 辗转相除求最大公约数
     * @param m 除数
     * @param n 被除数
     * @return
     */
    private int gcd(int m, int n) {
        if (n == 0) {
            return m;
        }
        int t = m % n;
        return gcd(n, t);
    }
}

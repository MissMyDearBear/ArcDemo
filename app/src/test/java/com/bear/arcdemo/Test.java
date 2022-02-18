package com.bear.arcdemo;

import com.bear.arcdemo.showcode.LruCache;

import java.util.regex.Pattern;

public class Test {


    @org.junit.Test
    public void test() {
        String test = "!this  1-s b8d!";
        String pattern = "[a-z]*([a-z]-[a-z])?[a-z]*[!,.]?";
        String[] array = test.split(" ");
        int count = 0;
        for (String word : array) {
            if (!word.isEmpty() && Pattern.matches(pattern, word)) {
                count++;
            }
        }
        int ss = count;

    }

    @org.junit.Test
    public void testLruCache() {
        LruCache<Integer> lruCache = new LruCache<>(5);
        for (int i = 0; i < 6; i++) {
            lruCache.push(String.valueOf(i), i);
        }
        int v2 = lruCache.get("2");


    }

    public int countValidWords(String sentence) {
        int ret = 0;
        String[] words = sentence.split(" ");
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            //多个空格分割
            if (word.isEmpty()) {
                continue;
            }
            if (markValid(word)) {
                ret++;
            }
        }
        return ret;
    }

    private boolean markValid(String word) {
        int count = 0;
        int link = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) >= '0' && word.charAt(i) <= '9') {
                return false;
            }
            if (word.charAt(i) == '!' || word.charAt(i) == '.' || word.charAt(i) == ',') {
                count++;
                if (count > 1 || i != (word.length() - 1)) {
                    return false;
                }
            }
            if (word.charAt(i) == '-') {
                link++;

                if (word.length() < 3) {
                    return false;
                }

                if (link > 1 || i == 0 || i == (word.length() - 1)) {
                    return false;
                }

                if (!(word.charAt(i - 1) >= 'a' && word.charAt(i - 1) <= 'z' && word.charAt(i + 1) >= 'a' && word.charAt(i + 1) <= 'z')) {
                    return false;
                }

            }
        }
        return true;
    }


}

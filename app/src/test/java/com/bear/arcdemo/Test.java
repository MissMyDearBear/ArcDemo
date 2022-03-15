package com.bear.arcdemo;

import com.bear.arcdemo.showcode.LruCache;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

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

    @org.junit.Test
    public void testRxJava() {
//        Flowable.just("Alen", "Bear").map(new Function<String, String>() {
//            @Override
//            public String apply(String s) throws Throwable {
//                String low = s.toLowerCase(Locale.ROOT);
//                System.out.print("first map >>>>>>> " + low + "\n");
//                return s.toLowerCase(Locale.ROOT);
//            }
//        }).subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Throwable {
//
//            }
//        });

        Flowable.just("Alen", "Bear").flatMap(new Function<String, Publisher<String>>() {
            @Override
            public Publisher<String> apply(String s) throws Throwable {
                System.out.print("do flatmap  >>>>>>> " + s + "\n");
                return Flowable.fromArray(s + ",");
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Throwable {
                System.out.print("map next >>>>>>> " + s + "\n");
            }
        });

    }

    @org.junit.Test
    public void testJump(){
        int[]nums = new int[]{3,2,1,0,4};
        boolean ret = canJump(nums);
        System.out.print("ret >>>>>>> " + ret + "\n");
    }

    public boolean canJump(int[] nums) {
        int n = nums.length;
        int maxIndex = 0;
        for(int i = 0;i<n;i++){
            if(maxIndex >= i){
                maxIndex = i+nums[i];
            }
        }
        return maxIndex >= n-1;
    }
    public char firstUniqChar(String s) {
        int n = s.length();
        if(n == 0){
            return ' ';
        }
        Map<Character,Integer> map = new HashMap<>();
        for(int i=0;i<n;i++){
            map.put(s.charAt(i),map.getOrDefault(s.charAt(i),0)+1);
        }
        for(int i=0;i<n;i++){
            if(map.get(s.charAt(i)) == 1){
                return s.charAt(i);
            }
        }
        return ' ';
    }


}

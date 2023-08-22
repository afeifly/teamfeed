package com.example;

import com.selffeed.util.FeedUtil;

import java.util.Random;

public class Test {
    public static int getRandomIconIndex() {
        Random random = new Random();
        return random.nextInt(46) + 1;
    }



        // 输出所有昵称

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.print(" " + getRandomIconIndex());
        }
        FeedUtil util = new FeedUtil();
//        util.checkUrl("https://chinadigitaltimes.net/chinese/feed");
        util.checkUrl("https://rsshub.exmm.top/t66y/20/");
    }
}

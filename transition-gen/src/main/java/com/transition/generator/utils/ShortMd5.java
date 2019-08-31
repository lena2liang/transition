package com.transition.generator.utils;

public class ShortMd5 {

    public static String shortMd5(String str) {
        if (str.length() < 16) {
            return str;
        }
        String str1 = str.substring(6) + str.substring(0, 6);
        return new StringBuilder(str1.toLowerCase()).reverse().toString().substring(0,16);
    }

    public static void main(String[] args) {
        System.out.println(shortMd5(""));
    }

}

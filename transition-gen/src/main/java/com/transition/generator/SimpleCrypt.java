package com.transition.generator;

public class SimpleCrypt {

    static long encrypt(long value, long key) {
        return value ^ key;
    }

    static long decrypt(long value, long key) {
        return value ^ key;
    }

}

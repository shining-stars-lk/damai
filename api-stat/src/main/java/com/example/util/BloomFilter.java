package com.example.util;


import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.function.BiFunction;
import java.util.logging.Logger;

public class BloomFilter {
    private static Logger log = Logger.getLogger(BloomFilter.class.toString());

    private final static int size = 1000000;
    private volatile  static BitSet bits = new BitSet(size);
    private final static int[] hashSeeds = new int[]{3, 113, 919, 2203, 4013, 6011};

    private final static List<BiFunction<String, Integer, Integer>> hashFuncs = new ArrayList<>();

    static {
        for (int i = 0; i < hashSeeds.length; i++) {
            hashFuncs.add((v, s) -> hash(v, s));
        }
    }

    public static void add(String value) {
        int len = hashFuncs.size();
        for (int i = 0; i <len ; i++) {
            BiFunction<String, Integer, Integer> hashFunc = hashFuncs.get(i);
            int hashSeed = hashSeeds[i];
            Integer bitHash = hashFunc.apply(value, hashSeed);
            bits.set(bitHash, true);
        }
    }

    public static boolean exists(String value) {
        int len = hashFuncs.size();
        for (int i = 0; i < len; i++) {
            BiFunction<String, Integer, Integer> hashFunc = hashFuncs.get(i);
            int hashSeed = hashSeeds[i];
            Integer bitHash = hashFunc.apply(value, hashSeed);
            boolean isTrue = bits.get(bitHash);
            if (isTrue == false) {
                return false;
            }
        }
        return true;
    }


    private static int hash(String value, int seed) {
        return Math.abs(value.hashCode() * seed + 33) % size;
    }
}

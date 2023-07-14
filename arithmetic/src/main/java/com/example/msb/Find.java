package com.example.msb;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-01-17
 **/
public class Find {

    /**
     * 在arr上，找满足>=value的最左位置
     * */
    public static int nearestIndex(int[] arr, int value) {
        int L = 0;
        int R = arr.length - 1;
        //记录最左的对号
        int index = -1;
        while (L <= R) {
            int mid = L + ((R - L) >> 1);
            if (arr[mid] >= value) {
                index = mid;
                R = mid - 1; 
            } else {
                L = mid + 1;
            }
        }
        return index;
    }
}



package com.example.atguigu;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-01-17
 **/
public class AtguiguSort {
    
    /**
     * 冒泡排序
     * */
    public void bubbleSort(int[] arr){
        for (int i = 0;i < arr.length - 1;i++) {
            for (int j = 0;j < arr.length - 1 - i;j++) {
                if (arr[j] > arr[j+1]) {
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
    }
}

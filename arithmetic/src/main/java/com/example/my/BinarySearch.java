package com.example.my;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-01-18
 **/
public class BinarySearch {
    
    /***
     * 基本得二分搜索
     */
    public int binarySearch(int[] nums, int target){
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (nums[mid] == target) {
                return mid;
            }else if (nums[mid] < target) {
                left = mid + 1;
            }else if (nums[mid] > target) {
                right = mid - 1;
            }
        }
        return -1;
    }
}
 
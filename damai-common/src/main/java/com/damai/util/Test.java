package com.damai.util;

import com.damai.test.TestDept;
import com.damai.test.TestUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 测试使用
 * @author: 阿宽不是程序员
 **/
public class Test {
    
    public static void main(String[] args) {
        run3();
    }
    
    public static void run1(){
        List<TestUser> testUserList = getTestUserList();
        List<TestDept> testDeptList = getTestDeptList();
        long start = System.currentTimeMillis();
        for (TestUser testUser : testUserList) {
            long deptId = testUser.getId();
            String userName = testUser.getName();
            for (TestDept testDept : testDeptList) {
                long id = testDept.getId();
                String name = testDept.getName();
                if (deptId == id) {
                    System.out.println("用户名为"+userName+" 的部门信息为:" + name);
                }
            }
        }
        System.out.println("执行1 耗时为：" + (System.currentTimeMillis() - start) + " ms");
    }
    
    public static void run2(){
        List<TestUser> testUserList = getTestUserList();
        List<TestDept> testDeptList = getTestDeptList();
        long start = System.currentTimeMillis();
        for (TestUser testUser : testUserList) {
            long deptId = testUser.getId();
            String userName = testUser.getName();
            for (TestDept testDept : testDeptList) {
                long id = testDept.getId();
                String name = testDept.getName();
                if (deptId == id) {
                    System.out.println("用户名为"+userName+" 的部门信息为:" + name);
                    break;
                }
            }
        }
        System.out.println("执行2 耗时为：" + (System.currentTimeMillis() - start) + " ms");
    }
    
    public static void run3(){
        List<TestUser> testUserList = getTestUserList();
        List<TestDept> testDeptList = getTestDeptList();
        Map<Long, TestDept> testDeptMap = 
                testDeptList.stream().collect(Collectors.toMap(TestDept::getId, testDept -> testDept, (v1, v2) -> v2));
        long start = System.currentTimeMillis();
        for (TestUser testUser : testUserList) {
            long deptId = testUser.getId();
            String userName = testUser.getName();
            
            TestDept testDept = testDeptMap.get(deptId);
            if (Objects.nonNull(testDept)) {
                String name = testDept.getName();
                System.out.println("用户名为"+userName+" 的部门信息为:" + name);
            }
        }
        System.out.println("执行3 耗时为：" + (System.currentTimeMillis() - start) + " ms");
    }
    
    public static List<TestUser> getTestUserList(){
        List<TestUser> testUsers = new ArrayList<>(100000);
        for (long i = 0; i < 100000; i++) {
            TestUser testUser = new TestUser();
            testUser.setId(i);
            testUser.setName("用户-" + i);
            testUser.setDeptId(i);
            testUsers.add(testUser);
        }
        return testUsers;
    }
    
    public static List<TestDept> getTestDeptList(){
        List<TestDept> testDepts = new ArrayList<>(50000);
        for (long i = 0; i < 50000; i++) {
            TestDept testDept = new TestDept();
            testDept.setId(i);
            testDept.setName("部门-" + i);
            testDepts.add(testDept);
        }
        return testDepts;
    }
}

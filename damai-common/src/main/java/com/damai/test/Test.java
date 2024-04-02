package com.damai.test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @program: damai
 * @description:
 * @author: k
 * @create: 2024-03-27
 **/
public class Test {
    
    public static void main(String[] args) {
        List<TestUser> testUserList = getTestUserList();
        List<String> testUserNameList = testUserList.stream().map(TestUser::getName).collect(Collectors.toList());
        testUserNameList.forEach((userName) -> System.out.println("用户名为：" + userName));
    }
    
    public static void testFunction(){
        Function<Integer,String> function = str -> "转换后："+str;
        System.out.println(function.apply(1));
    }
    
    public static void testConsumer(){
        Consumer<String> consumer = str -> System.out.println("输出："+str);
        consumer.accept("这是我");
    }
    
    public static void testSupplier(){
        Supplier<String> supplier = () -> "提供数据";
        System.out.println(supplier.get());
    }
    
    public static void testPredicate(){
        Predicate<Integer> predicate = (number) -> 1 == number;
        System.out.println("是否等于1:"+predicate.test(2));
    }
    
    public static List<TestUser> getTestUserList(){
        List<TestUser> testUsers = new ArrayList<>(100);
        for (long i = 0; i < 100; i++) {
            TestUser testUser = new TestUser();
            testUser.setId(i);
            testUser.setName("用户-" + i);
            testUser.setDeptId(i);
            testUsers.add(testUser);
        }
        return testUsers;
    }
}

package com.example.jdk8;

import com.example.entity.User;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Case1 {

    public static void main(String[] args) {
        TestStream();
    }

    public static void TestStream(){
        List<User> users = Arrays.asList(new User(1, 20,"张三"), new User(2, 20,"李四"));
        List<User> users2 = users.stream().filter(user -> user.getId() > 1).collect(Collectors.toList());
        users2.forEach(System.out::println);

        List<String> userNames = users.stream().map(user -> user.getName()).collect(Collectors.toList());
        userNames.forEach(System.out::println);

        List<Integer> userAges = users.stream().map(user -> user.getAge()).distinct().collect(Collectors.toList());
        userAges.forEach(System.out::println);
    }
}

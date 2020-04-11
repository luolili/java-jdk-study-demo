package com.luo.java8;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StreamUse {


    private static List<UserResponse> user2Resp(List<User> userList) {

        return userList.stream().map(UserResponse::new).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        List<User> userList = new ArrayList<>();
        userList.add(new User(1L, "we", "rt", 1));
        userList.add(new User(2L, "tu", "yb", 1));
        userList.add(new User(3L, "wu", "c", 0));
        user2Resp(userList).stream().forEach(System.out::println);


    }
}

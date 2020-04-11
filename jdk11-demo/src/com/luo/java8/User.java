package com.luo.java8;

public class User {
    private Long id;
    private String name;
    private String password;
    private Integer sex;

    public Long getId() {
        return id;
    }

    public User(Long id, String name, String password, Integer sex) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.sex = sex;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }
}

package com.luo.java8;

public class UserResponse {
    private Long id;
    private String name;
    private Integer sex;

    public UserResponse() {
    }

    public UserResponse(Long id, String name, Integer sex) {
        this.id = id;
        this.name = name;
        this.sex = sex;
    }

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.sex = user.getSex();
    }

    public Long getId() {
        return id;
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

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                '}';
    }
}

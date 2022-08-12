package com.tedu.springboot2206.entity;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 42L;
    private String Username;
    private String pwd;
    private String nickname;
    private int age;

    public User(){}

    public User(String username, String pwd, String nickname, int age) {
        Username = username;
        this.pwd = pwd;
        this.nickname = nickname;
        this.age = age;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "Username='" + Username + '\'' +
                ", pwd='" + pwd + '\'' +
                ", nickname='" + nickname + '\'' +
                ", age=" + age +
                '}';
    }
}

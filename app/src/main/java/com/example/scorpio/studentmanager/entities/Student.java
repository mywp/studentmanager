package com.example.scorpio.studentmanager.entities;

/**
 * Created by Scorpio on 16/1/17.
 */
public class Student {
    public Student() {
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                '}';
    }

    public Student( String name, String sex,Integer age) {
        super();
        this.age = age;
        this.sex = sex;
        this.name = name;
    }

    private String name;
    private String sex;
    private Integer age;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    
}

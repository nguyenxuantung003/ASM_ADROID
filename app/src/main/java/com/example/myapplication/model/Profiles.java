package com.example.myapplication.model;

public class Profiles {
    private String gender;
    private int age;
    private int height;
    private int weight;
    private double bmi;
    private String createdAt;

    public Profiles() {
    }

    public Profiles(String gender, int age, int height, int weight, double bmi, String createdAt) {
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.bmi = bmi;
        this.createdAt = createdAt;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

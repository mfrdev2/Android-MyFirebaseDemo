package com.frabbi.myfirebasedemo.model;

public class StudentModel {
    private int roll;
    private String name;
    private String course;
    private int duration;
    private String imagePath;

    public StudentModel() {
    }

    public StudentModel(int roll, String name, String course, int duration) {
        this.roll = roll;
        this.name = name;
        this.course = course;
        this.duration = duration;
    }

    public StudentModel(int roll, String name, String course, int duration, String imagePath) {
        this.roll = roll;
        this.name = name;
        this.course = course;
        this.duration = duration;
        this.imagePath = imagePath;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "StudentModel{" +
                "roll=" + roll +
                ", name='" + name + '\'' +
                ", course='" + course + '\'' +
                ", duration=" + duration +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}

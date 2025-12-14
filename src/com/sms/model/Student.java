package com.sms.model;

import java.sql.Timestamp;

/**
 * Model class representing a Student record.
 * Maps directly to the 'students' database table.
 */
public class Student {

    private int id;
    private String rollNo;
    private String name;
    private String course;
    private int marks;
    private Timestamp createdAt;

    // Constructor for INSERT (no id, no createdAt)
    public Student(String rollNo, String name, String course, int marks) {
        this.rollNo = rollNo;
        this.name = name;
        this.course = course;
        this.marks = marks;
    }

    // Constructor for SELECT / UPDATE (all fields)
    public Student(int id, String rollNo, String name, String course, int marks, Timestamp createdAt) {
        this.id = id;
        this.rollNo = rollNo;
        this.name = name;
        this.course = course;
        this.marks = marks;
        this.createdAt = createdAt;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
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

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Student [id=" + id +
                ", rollNo=" + rollNo +
                ", name=" + name +
                ", course=" + course +
                ", marks=" + marks +
                ", createdAt=" + createdAt + "]";
    }
}

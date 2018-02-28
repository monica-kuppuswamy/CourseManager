package com.example.monic.coursemanager;

import java.io.Serializable;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by monic on 11/6/2017.
 */

public class Course extends RealmObject implements Serializable{

    @PrimaryKey
    private int id;
    private String userName;
    private String title;
    private int instructorId;
    private String day;
    private String time;
    private int credits;
    private String semester;

    public Course(String userName, String title, int instructorId, String day, String time, int credits, String semester) {
        this.userName = userName;
        this.title = title;
        this.instructorId = instructorId;
        this.day = day;
        this.time = time;
        this.credits = credits;
        this.semester = semester;
    }

    public Course() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;
        return title.toLowerCase().equals(course.title.toLowerCase());

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + userName.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + instructorId;
        result = 31 * result + day.hashCode();
        result = 31 * result + time.hashCode();
        result = 31 * result + credits;
        result = 31 * result + semester.hashCode();
        return result;
    }
}

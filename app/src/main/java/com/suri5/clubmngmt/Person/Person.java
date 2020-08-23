package com.suri5.clubmngmt.Person;

import android.graphics.Bitmap;

public class Person {
    int pk;
    String name;
    int id_num;
    String Major;
    String Gender;
    String Birthday;
    String Email;
    String Mobile;
    Bitmap picture;

    public Person() {
    }

    public Person(String name, int id_num, String major, String gender, String birthday, String email, String mobile, Bitmap picture) {
        this.name = name;
        this.id_num = id_num;
        Major = major;
        Gender = gender;
        Birthday = birthday;
        Email = email;
        Mobile = mobile;
        this.picture = picture;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId_num() {
        return id_num;
    }

    public void setId_num(int id_num) {
        this.id_num = id_num;
    }

    public String getMajor() {
        return Major;
    }

    public void setMajor(String major) {
        Major = major;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }
}
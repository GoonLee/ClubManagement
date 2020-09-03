package com.suri5.clubmngmt.Person;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.suri5.clubmngmt.Group.Group;

public class Person implements Parcelable {
    int pk;
    String name;
    int id_num;
    String Major;
    String Gender;
    String Birthday;
    String Email;
    String Mobile;
    Bitmap picture;
    Boolean isChecked = false;

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

    public Person(Parcel in){
        readFromParcel(in);
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

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>(){
        public Person createFromParcel(Parcel in){
            return new Person(in);
        }

        @Override
        public Person[] newArray(int i) {
            return new Person[i];
        }
    };
    public void readFromParcel(Parcel in){
        pk = in.readInt();
        name = in.readString();
        id_num = in.readInt();
        Major = in.readString();
        Gender = in.readString();
        Birthday = in.readString();
        Email = in.readString();
        Mobile = in.readString();
        picture = in.readParcelable(Bitmap.class.getClassLoader());
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(pk);
        parcel.writeString(name);
        parcel.writeInt(id_num);
        parcel.writeString(Major);
        parcel.writeString(Gender);
        parcel.writeString(Birthday);
        parcel.writeString(Email);
        parcel.writeString(Mobile);
        parcel.writeParcelable(picture,i);
    }
}
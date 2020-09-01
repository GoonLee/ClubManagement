package com.suri5.clubmngmt.Schedule;

import android.os.Parcel;
import android.os.Parcelable;

import com.suri5.clubmngmt.Group.Group;

import java.io.Serializable;

public class Schedule implements Parcelable {
    int key;
    String title;
    String startDate;
    String startTime;
    String endDate;
    String endTime;
    String place;
    String comment;

    public Schedule(){
    }

    public Schedule(Parcel in){
        readFromParcel(in);
    }

    public Schedule(int key, String title, String startDate, String startTime, String endDate, String endTime, String place, String comment) {
        this.key = key;
        this.title = title;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.place = place;
        this.comment = comment;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public static final Parcelable.Creator<Schedule> CREATOR = new Parcelable.Creator<Schedule>(){
        public Schedule createFromParcel(Parcel in){
            return new Schedule(in);
        }

        @Override
        public Schedule[] newArray(int i) {
            return new Schedule[i];
        }
    };

    public void readFromParcel(Parcel parcel){
        key = parcel.readInt();
        title = parcel.readString();
        startDate = parcel.readString();
        startTime = parcel.readString();
        endDate = parcel.readString();
        endTime = parcel.readString();
        place = parcel.readString();
        comment = parcel.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(key);
        parcel.writeString(title);
        parcel.writeString(startDate);
        parcel.writeString(startTime);
        parcel.writeString(endDate);
        parcel.writeString(endTime);
        parcel.writeString(place);
        parcel.writeString(comment);


    }
}
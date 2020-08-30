package com.suri5.clubmngmt.Group;

import android.os.Parcel;
import android.os.Parcelable;


public class Group implements Parcelable {
    int Key;
    String Name;
    int totalNum;

    public Group(){ }
    public Group(String name){
        this.Name = name;
        this.totalNum = 0;
    }
    public Group(String name, int totalNum){
        this.Name = name;
        this.totalNum = totalNum;
    }
    public Group(Parcel in){
        readFromParcel(in);
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getKey() {
        return Key;
    }

    public void setKey(int key) {
        Key = key;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>(){
        public Group createFromParcel(Parcel in){
            return new Group(in);
        }

        @Override
        public Group[] newArray(int i) {
            return new Group[i];
        }
    };
    public void readFromParcel(Parcel in){
        Key = in.readInt();
        Name = in.readString();
        totalNum = in.readInt();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(Key);
        parcel.writeString(Name);
        parcel.writeInt(totalNum);
    }
}

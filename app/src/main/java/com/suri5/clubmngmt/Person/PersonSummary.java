package com.suri5.clubmngmt.Person;

public class PersonSummary {
    //Todo : Check necessary field
    String name;
    int id_num;

    public PersonSummary(String name, int id_num) {
        this.name = name;
        this.id_num = id_num;
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
}
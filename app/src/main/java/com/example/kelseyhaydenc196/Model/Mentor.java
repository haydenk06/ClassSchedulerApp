package com.example.kelseyhaydenc196.Model;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "mentor_table", indices = {@Index("mentorId")})
public class Mentor {
    @PrimaryKey(autoGenerate = true)
    private int mentorId;
    private String name;
    private String phoneNumber;
    private String email;

    public Mentor(String name, String phoneNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    //Getters
    public int getMentorId() {
        return mentorId;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    //Setters
    public void setMentorId(int mentorId) {
        this.mentorId = mentorId;
    }
}

package com.example.kelseyhaydenc196.Model;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.kelseyhaydenc196.Utilities.Dates;

import java.util.Date;

@Entity(tableName = "course_table", indices = {@Index("id")})
public class Course {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String courseTitle;
    @TypeConverters(Dates.class)
    private Date startDate;
    @TypeConverters(Dates.class)
    private Date endDate;
    private String status;
    private String note;

    private Integer termId;
    private Integer mentorId;

    public Course(String courseTitle, Date startDate, Date endDate, String status, String note, int termId, int mentorId) {
        this.courseTitle = courseTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.note = note;
        this.termId = termId;
        this.mentorId = mentorId;
    }

    //Getters
    public int getId() {
        return id;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    public String getNote() {
        return note;
    }

    public Integer getTermId() {
        return termId;
    }

    public Integer getMentorId() {
        return mentorId;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setMentorId(Integer mentorId) {
        this.mentorId = mentorId;
    }

    public void setTermId(Integer termId) {
        this.termId = termId;
    }

}

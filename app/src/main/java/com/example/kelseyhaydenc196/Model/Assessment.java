package com.example.kelseyhaydenc196.Model;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.kelseyhaydenc196.Utilities.Dates;

import java.util.Date;

@Entity(tableName = "assessment_table", indices = {@Index("assessmentId")})

public class Assessment {
    @PrimaryKey(autoGenerate = true)

    private int assessmentId;
    private String assessmentTitle;
    private String assessmentType;
    @TypeConverters(Dates.class)
    private Date dueDate;
    private int courseId;

    public Assessment(String assessmentTitle, String assessmentType, Date dueDate, int courseId) {
        this.assessmentTitle = assessmentTitle;
        this.assessmentType = assessmentType;
        this.dueDate = dueDate;
        this.courseId = courseId;
    }

    //Getters
    public int getAssessmentId() {
        return assessmentId;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public String getAssessmentTitle() {
        return assessmentTitle;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public int getCourseId() {
        return courseId;
    }

    //Setters
    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

}

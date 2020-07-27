package com.example.kelseyhaydenc196.Model;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.kelseyhaydenc196.Utilities.Dates;

import java.util.Date;

@Entity(tableName = "term_table", indices = {@Index("termId")})
public class Term {
    @PrimaryKey(autoGenerate = true)
    private int termId;
    private String termTitle;
    @TypeConverters(Dates.class)
    private Date termStart;
    @TypeConverters(Dates.class)
    private Date termEnd;

    public Term(String termTitle, Date termStart, Date termEnd) {
        this.termTitle = termTitle;
        this.termStart = termStart;
        this.termEnd = termEnd;
    }

    //Getters
    public int getTermId() {
        return termId;
    }

    public String getTermTitle() {
        return termTitle;
    }

    public Date getTermStart() {
        return termStart;
    }

    public Date getTermEnd() {
        return termEnd;
    }

    //Setters
    public void setTermId(int termId) {
        this.termId = termId;
    }
}

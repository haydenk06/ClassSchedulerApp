package com.example.kelseyhaydenc196.Utilities;

import com.example.kelseyhaydenc196.Model.Assessment;
import com.example.kelseyhaydenc196.Model.Course;
import com.example.kelseyhaydenc196.Model.Mentor;
import com.example.kelseyhaydenc196.Model.Term;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("ALL")
public class SampleData {

    public static List<Term> getTerms() {
        List<Term> terms = new ArrayList<>();
        terms.add(new Term("Winter Term", new Date(2020, 1, 1), new Date(2020, 3, 30)));
        terms.add(new Term("Spring Term", new Date(2020, 4, 1), new Date(2020, 6, 30)));
        terms.add(new Term("Summer Term", new Date(2020, 7, 1), new Date(2020, 9, 30)));
        terms.add(new Term("Fall Term", new Date(2020, 10, 1), new Date(2020, 12, 30)));
        return terms;
    }

    public static List<Course> getCourses() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("Project Management", new Date(2020, 1, 1), new Date(2020, 2, 30), "Completed", "Certification course with Comptia", 1, 2));
        courses.add(new Course("Mobile App", new Date(2020, 5, 1), new Date(2020, 6, 15), "IN-PROGRESS", "Build a class tracking Android application with Java", 2, 3));
        courses.add(new Course("Capstone", new Date(2020, 9, 1), new Date(2020, 9, 30), "Planned", " Final project encapsulating entire knowledge learned through the program.", 3, 2));
        courses.add(new Course("Data Management", new Date(2020, 7, 1), new Date(2020, 8, 30), "Dropped", "", 3, 2));
        courses.add(new Course("Science", new Date(2020, 10, 15), new Date(2020, 11, 20), "Planned", " Science Labs", 4, 1));
        courses.add(new Course("Math", new Date(2020, 11, 20), new Date(2020, 12, 30), "Planned", "Advanced Trigonometry", 4, 3));

        return courses;
    }

    public static List<Assessment> getAssessments() {
        List<Assessment> assessments = new ArrayList<>();
        assessments.add(new Assessment("Sample 1", "Objective Assessment", new Date(2020, 2, 18), 1));
        assessments.add(new Assessment("Sample 2", "Objective Assessment", new Date(2020, 6, 10), 2));
        assessments.add(new Assessment(" Sample 3", "Performance Assessment", new Date(2020, 9, 28), 3));
        assessments.add(new Assessment(" Sample 4", "Performance Assessment", new Date(2020, 2, 29), 1));
        assessments.add(new Assessment(" Sample 5", "Objective Assessment", new Date(2020, 8, 23), 4));
        assessments.add(new Assessment(" Sample 6", "Performance Assessment", new Date(2020, 6, 14), 2));
        assessments.add(new Assessment(" Sample 7", "Performance Assessment", new Date(2020, 11, 12), 5));
        assessments.add(new Assessment(" Sample 8", "Objective Assessment", new Date(2020, 12, 23), 6));
        return assessments;
    }

    public static List<Mentor> getMentors() {
        List<Mentor> mentors = new ArrayList<>();
        mentors.add(new Mentor("Robert Baratheon", "971-123-4567", "King@got.com"));
        mentors.add(new Mentor("Sansa Stark", "503-111-5677", "Sansa@winterfell.co"));
        mentors.add(new Mentor("John Snow", "313-4564-2349", "js@icloud.com"));
        return mentors;
    }
}

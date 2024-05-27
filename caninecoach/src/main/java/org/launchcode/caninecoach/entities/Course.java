package org.launchcode.caninecoach.entities;

import jakarta.persistence.*;

import java.util.ArrayList;


@Entity
public class Course extends AbstractEntity {


    @OneToOne(mappedBy = "course")
    @JoinColumn(name = "course_id")
    private CourseInfo courseInfo;


    @OneToOne
    @JoinColumn(name = "curriculum_id")
    private Curriculum curriculum;

    private String name;

    public Course(CourseInfo courseInfo, Curriculum curriculum, String name) {
        this.courseInfo = courseInfo;
        this.curriculum = curriculum;
        this.name = name;
    }


    public Course() {

    }


    public CourseInfo getCourseInfo() {
        return courseInfo;
    }

    public void setCourseInfo(CourseInfo courseInfo) {
        this.courseInfo = courseInfo;
    }

    public Curriculum getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(Curriculum curriculum) {
        this.curriculum = curriculum;
    }

    public String getName() {
        return name;
    }
}

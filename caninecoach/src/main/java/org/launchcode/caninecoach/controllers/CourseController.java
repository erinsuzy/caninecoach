package org.launchcode.caninecoach.controllers;

import org.launchcode.caninecoach.entities.Course;
import org.launchcode.caninecoach.entities.User;
import org.launchcode.caninecoach.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> createCourse(@RequestParam Course course) {
    course = new Course();
    courseService.createCourse(course);
    return ResponseEntity.ok("Course created");

    }

    @PostMapping("/update")
    public ResponseEntity<String> updateCourse(@RequestParam Course course) {
        courseService.updateCourse(course);
        return ResponseEntity.ok("Course updated");
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteCourse(@RequestParam Course course) {
        courseService.deleteCourse(course);
        return ResponseEntity.ok("Course deleted");
    }

}

package com.bugbusters.course.controller;

import com.bugbusters.course.dto.CourseCreateRequest;
import com.bugbusters.course.dto.CourseResponse;
import com.bugbusters.course.dto.ReviewCreateRequest;
import com.bugbusters.course.dto.ReviewResponse;
import com.bugbusters.course.service.CourseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/course")
public class CourseController{

    @Autowired
    private final CourseService courseService;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public CourseResponse createCourse(@RequestBody CourseCreateRequest request) {
        log.info("Creating course, name: {}", request.name());
        return courseService.createCourse(request);
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<CourseResponse> getAllCourses() {
        log.info("Getting all courses");
        return courseService.getAllCourses();
    }

    @GetMapping("/{courseId}/review")
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewResponse> getCourseReviews(@PathVariable(name = "courseId") Long courseId) {
        log.info("Getting all reviews for course: {}", courseId);
        Long userId = 1L;
        return courseService.getCourseReviews(courseId, userId);
    }

    @PostMapping("/{courseId}/review")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponse postCourseReviews(@PathVariable(name = "courseId") Long courseId, @RequestBody ReviewCreateRequest request) {
        log.info("Getting all reviews for course: {}", courseId);
        Long userId = 1L;
        return courseService.createCourseReview(request, courseId, userId);
    }


}

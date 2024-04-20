package com.bugbusters.course.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.bugbusters.course.dto.Course.CourseCreateRequest;
import com.bugbusters.course.dto.Course.CourseResponse;
import com.bugbusters.course.dto.Course.CourseUpdateRequest;
import com.bugbusters.course.service.CourseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private final CourseService courseService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public CourseResponse createCourse(@RequestBody CourseCreateRequest request) {
        log.info("Creating course, name: {}", request.name());
        // we may need to validate if the user is an instructor
        Long instructorId = 1L; // will be gotten from the bearer token
        return courseService.createCourse(request, instructorId);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<CourseResponse> getAllCourses() {
        log.info("Getting all courses");
        return courseService.getAllCourses();
    }

    @GetMapping("/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    // @Cacheable("course")
    public CourseResponse getCourse(@PathVariable("courseId") String courseId) {
        log.info("Getting course with id: {}", courseId);
        return courseService.getCourse(courseId);
    }

    @PutMapping("/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    // @CachePut("course")
    public ResponseEntity<CourseResponse> updateCourse(@PathVariable("courseId") Long courseId,
            @RequestBody CourseUpdateRequest request) {
        log.info("Updating course with id: {}", courseId);
        Long instructorId = 1L; // will be gotten from the bearer token
        return courseService.updateCourse(courseId, request, instructorId);
    }

    @DeleteMapping("/{courseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // @CacheEvict("course")
    public ResponseEntity<?> deleteCourse(@PathVariable("courseId") String courseId) {
        log.info("Deleting course with id: {}", courseId);
        Long instructorId = 1L; // will be gotten from the bearer token
        return courseService.deleteCourse(courseId, instructorId);
    }
}

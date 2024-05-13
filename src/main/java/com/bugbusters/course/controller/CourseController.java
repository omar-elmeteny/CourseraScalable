package com.bugbusters.course.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
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
import com.bugbusters.course.messages.services.CommandDispatcher;
import com.bugbusters.course.service.CourseService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RestController
@RequestMapping("/api/course")
@AllArgsConstructor
@EnableCaching
// You have to set the value, which is the name of the cache you want to store
// the data in, as well as the key for which the data will be stored in.
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

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> searchCourses(
            @RequestParam("query") String query) {
        log.info("Searching courses with query: {}", query);
        List<CourseResponse> courses = courseService.searchCourses(query);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/filter")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> filterCourses(
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "minRating", required = false) Double minRating) {
        log.info("Filtering courses with minPrice: {}, maxPrice: {}, category: {}, minRating: {}",
                minPrice, maxPrice, category, minRating);

        if (minPrice == null && maxPrice == null && category == null && minRating == null) {
            return ResponseEntity.badRequest().body("At least one of the parameters should be provided");
        }

        List<CourseResponse> courses = courseService.filterCourses(
                Optional.ofNullable(minPrice),
                Optional.ofNullable(maxPrice),
                Optional.ofNullable(category),
                Optional.ofNullable(minRating));
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    @Cacheable(key = "#courseId", value = "Course")
    public CourseResponse getCourse(@PathVariable("courseId") String courseId) {
        log.info("Getting course with id: {}", courseId);
        return courseService.getCourse(courseId);
    }

    @PutMapping("/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    @CachePut(key = "#courseId", value = "Course")
    public ResponseEntity<CourseResponse> updateCourse(@PathVariable("courseId") Long courseId,
            @RequestBody CourseUpdateRequest request) {
        log.info("Updating course with id: {}", courseId);
        Long instructorId = 1L; // will be gotten from the bearer token
        return courseService.updateCourse(courseId, request, instructorId);
    }

    @DeleteMapping("/{courseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(key = "#courseId")
    public ResponseEntity<?> deleteCourse(@PathVariable("courseId") String courseId) {
        log.info("Deleting course with id: {}", courseId);
        Long instructorId = 1L; // will be gotten from the bearer token
        return courseService.deleteCourse(courseId, instructorId);
    }

}

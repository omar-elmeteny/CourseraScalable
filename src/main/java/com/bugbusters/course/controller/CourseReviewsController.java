package com.bugbusters.course.controller;

import com.bugbusters.course.dto.CourseReview.ReviewResponse;
import com.bugbusters.course.service.CourseReviewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import com.bugbusters.course.dto.CourseReview.ReviewRequest;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/course")
public class CourseReviewsController {

    @Autowired
    private final CourseReviewService courseReviewService;

    @GetMapping("/{courseId}/review")
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewResponse> getCourseReviews(@PathVariable(name = "courseId") Long courseId) {
        log.info("Getting all reviews for course: {}", courseId);
        return courseReviewService.getCourseReviews(courseId);
    }

    @PostMapping("/{courseId}/review")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponse createCourseReview(@PathVariable(name = "courseId") Long courseId,
            @RequestBody ReviewRequest request) {
        log.info("Getting all reviews for course: {}", courseId);
        Long userId = 1L; // will be gotten from the bearer token
        return courseReviewService.createCourseReview(request, courseId, userId);
    }

    @DeleteMapping("/{courseId}/review/{reviewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteCourseReview(@PathVariable(name = "courseId") Long courseId,
            @PathVariable(name = "reviewId") Long reviewId) {
        log.info("Deleting review with id: {}", reviewId);
        Long userId = 1L;
        return courseReviewService.deleteCourseReview(courseId, reviewId, userId);
    }

    @PutMapping("/{courseId}/review/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ReviewResponse> updateCourseReview(@PathVariable(name = "courseId") Long courseId,
            @PathVariable(name = "reviewId") Long reviewId, @RequestBody ReviewRequest request) {
        log.info("Updating review with id: {}", reviewId);
        Long userId = 1L;
        return courseReviewService.updateCourseReview(request, courseId, reviewId,
                userId);
    }

}

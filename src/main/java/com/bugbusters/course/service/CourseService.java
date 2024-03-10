package com.bugbusters.course.service;

import com.bugbusters.course.dto.CourseCreateRequest;
import com.bugbusters.course.dto.CourseResponse;
import com.bugbusters.course.dto.ReviewCreateRequest;
import com.bugbusters.course.dto.ReviewResponse;
import com.bugbusters.course.models.course.Course;
import com.bugbusters.course.models.course_review.CourseReview;
import com.bugbusters.course.repository.CourseRepository;
import com.bugbusters.course.repository.CourseReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@lombok.extern.slf4j.Slf4j
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseReviewRepository CourseReviewRepository;
    public CourseResponse createCourse(CourseCreateRequest request) {
        Course course = Course.builder()
                .name(request.name())
                .description(request.description())
                .instructorId(request.instructorId())
                .price(request.price())
                .categories(request.categories())
                .build();
        courseRepository.save(course);
        log.info("Course created successfully");
        return mapFromCourseToCourseResponse(course);
    }

    public List<CourseResponse> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream().map(this::mapFromCourseToCourseResponse).toList();
    }

    public ReviewResponse createCourseReview(ReviewCreateRequest request, Long courseId, Long userId) {
        Optional<Course> course = courseRepository.findById(courseId);
        CourseReview review = CourseReview.builder()
                .course(course.orElseThrow())
                .userId(userId)
                .comment(request.comment())
                .date(new java.util.Date())
                .build();
        CourseReviewRepository.save(review);
        log.info("Review created successfully");
        return mapFromCourseReviewToReviewResponse(review);
    }

    public CourseResponse mapFromCourseToCourseResponse(Course course) {
        return new CourseResponse(course.getId(), course.getName(), course.getDescription(), course.getInstructorId(), course.getPrice(), course.getRating(), course.getCategories(), course.getStatus());
    }

    public List<ReviewResponse> getCourseReviews(Long courseId, Long userId) {
        List<CourseReview> reviews = CourseReviewRepository.findAllByCourseIdAndUserId(courseId, userId);
        return reviews.stream().map(this::mapFromCourseReviewToReviewResponse).toList();
    }

    public ReviewResponse mapFromCourseReviewToReviewResponse(CourseReview review) {
        Course course = review.getCourse();
        return new ReviewResponse(review.getId(), course.getId(), review.getUserId(), review.getComment(), review.getDate());

    }

}

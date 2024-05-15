package com.bugbusters.course.service;

import com.bugbusters.course.dto.CourseReview.ReviewRequest;
import com.bugbusters.course.dto.CourseReview.ReviewResponse;
import com.bugbusters.course.models.course.Course;
import com.bugbusters.course.models.course_review.CourseReview;
import com.bugbusters.course.repository.CourseRepository;
import com.bugbusters.course.repository.CourseReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
@lombok.extern.slf4j.Slf4j
public class CourseReviewService {

    private final CourseRepository courseRepository;
    private final CourseReviewRepository CourseReviewRepository;

    public ReviewResponse createCourseReview(ReviewRequest request, Long courseId, Long userId) {
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

    public List<ReviewResponse> getCourseReviews(Long courseId) {
        List<CourseReview> reviews = CourseReviewRepository.findAllByCourseId(courseId);
        return reviews.stream().map(this::mapFromCourseReviewToReviewResponse).toList();
    }

    public ReviewResponse mapFromCourseReviewToReviewResponse(CourseReview review) {
        Course course = review.getCourse();
        return new ReviewResponse(review.getId(), course.getId(), review.getUserId(),
                review.getComment(),
                review.getDate());

    }

    public ResponseEntity<Void> deleteCourseReview(Long courseId, Long reviewId,
            Long userId) {
        try {
            CourseReview review = CourseReviewRepository.findById(reviewId).orElseThrow();
            if (review.getUserId().equals(userId)) {
                CourseReviewRepository.delete(review);
                log.info("Review deleted successfully");
                return ResponseEntity.noContent().build();
            } else {
                log.error("User is not authorized to delete this review");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (NoSuchElementException e) {
            log.error("Review not found");
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("An error occurred while deleting the review");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<ReviewResponse> updateCourseReview(ReviewRequest request, Long courseId, Long reviewId,
            Long userId) {
        try {
            CourseReview review = CourseReviewRepository.findById(reviewId).orElseThrow();
            if (review.getUserId().equals(userId)) {
                review.setComment(request.comment());
                CourseReviewRepository.save(review);
                log.info("Review updated successfully");
                ReviewResponse response = mapFromCourseReviewToReviewResponse(review);
                return ResponseEntity.ok(response);
            } else {
                log.error("User is not authorized to update this review");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (NoSuchElementException e) {
            log.error("Review not found");
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("An error occurred while updating the review");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

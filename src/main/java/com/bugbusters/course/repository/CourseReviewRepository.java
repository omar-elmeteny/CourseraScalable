package com.bugbusters.course.repository;

import com.bugbusters.course.models.course_review.CourseReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseReviewRepository extends JpaRepository<CourseReview, Long> {
    List<CourseReview> findAllByCourseIdAndUserId(Long courseId, Long userId);
}

package com.bugbusters.course.repository;

import com.bugbusters.course.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}

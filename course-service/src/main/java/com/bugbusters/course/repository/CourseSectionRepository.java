package com.bugbusters.course.repository;

import com.bugbusters.course.models.course_section.CourseSection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseSectionRepository extends JpaRepository<CourseSection, UUID> {

    List<CourseSection> findAllByCourseId(Long courseId);

}

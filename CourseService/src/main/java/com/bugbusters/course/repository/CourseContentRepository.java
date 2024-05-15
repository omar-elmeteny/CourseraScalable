package com.bugbusters.course.repository;

import com.bugbusters.course.models.course_content.CourseContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseContentRepository extends JpaRepository<CourseContent, UUID> {

    List<CourseContent> findAllBySectionId(UUID sectionId);

    void deleteBySectionId(UUID sectionId);

}

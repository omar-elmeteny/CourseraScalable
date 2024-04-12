package com.bugbusters.course.repository;

import com.bugbusters.course.models.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findCourseById(Long courseId);

    void deleteCourseById(Long courseId);

    @Query("UPDATE Course c SET c.name = :name, c.description = :description, c.price = :price WHERE c.id = :courseId")
    Course updateCourseById(@Param("courseId") Long courseId, @Param("name") String name,
            @Param("description") String description,
            @Param("price") double price);
}

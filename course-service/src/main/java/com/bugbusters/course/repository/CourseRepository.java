package com.bugbusters.course.repository;

import com.bugbusters.course.models.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
        Course findCourseById(Long courseId);

        void deleteCourseById(Long courseId);

        @Query(value = "SELECT c.* FROM course c " +
                        "WHERE LOWER(c.name) LIKE %:query% " +
                        "OR LOWER(c.description) LIKE %:query% " +
                        "OR LOWER(cast(c.categories as text)) LIKE %:query%", nativeQuery = true)
        List<Course> searchCourses(@Param("query") String query);

        @Query(value = "SELECT c.* FROM Course c WHERE " +
                        "(:minPrice IS NULL OR c.price >= :minPrice) " +
                        "AND (:maxPrice IS NULL OR c.price <= :maxPrice) " +
                        "AND (:minRating IS NULL OR c.rating >= :minRating) " +
                        "AND (:category IS NULL OR LOWER(cast(c.categories as text)) LIKE %:category%)", nativeQuery = true)
        List<Course> filterCourses(
                        @Param("minPrice") Optional<Double> minPrice,
                        @Param("maxPrice") Optional<Double> maxPrice,
                        @Param("category") Optional<String> category,
                        @Param("minRating") Optional<Double> minRating);

        @Query("UPDATE Course c SET c.name = :name, c.description = :description, c.price = :price WHERE c.id = :courseId")
        Course updateCourseById(@Param("courseId") Long courseId, @Param("name") String name,
                        @Param("description") String description,
                        @Param("price") double price);
}

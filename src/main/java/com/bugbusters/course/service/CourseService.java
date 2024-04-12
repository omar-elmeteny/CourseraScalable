package com.bugbusters.course.service;

import com.bugbusters.course.dto.Course.CourseCreateRequest;
import com.bugbusters.course.dto.Course.CourseResponse;
import com.bugbusters.course.dto.Course.CourseUpdateRequest;
import com.bugbusters.course.models.course.Course;
import com.bugbusters.course.repository.CourseRepository;
import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@lombok.extern.slf4j.Slf4j
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseResponse createCourse(CourseCreateRequest request, Long instructorId) {
        Course course = Course.builder()
                .name(request.name())
                .description(request.description())
                .instructorId(instructorId)
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

    public CourseResponse mapFromCourseToCourseResponse(Course course) {
        return new CourseResponse(course.getId(), course.getName(), course.getDescription(), course.getInstructorId(),
                course.getPrice(), course.getRating(), course.getCategories(), course.getStatus());
    }

    public CourseResponse getCourse(String courseId) {
        Course course = courseRepository.findById(Long.parseLong(courseId)).orElseThrow();
        return mapFromCourseToCourseResponse(course);
    }

    public ResponseEntity<CourseResponse> updateCourse(Long courseId, CourseUpdateRequest request,
            Long instructorId) {
        try {
            Course course = courseRepository.findById(courseId).orElseThrow();

            if (!course.getInstructorId().equals(instructorId)) {
                log.error("User is not authorized to update this course");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            course.setCategories(request.categories().orElse(course.getCategories()));
            course.setDescription(request.description().orElse(course.getDescription()));
            course.setName(request.name().orElse(course.getName()));
            course.setPrice(request.price().orElse(course.getPrice()));
            course.setStatus(request.status().orElse(course.getStatus()));
            courseRepository.save(course);
            log.info("Course updated successfully");

            CourseResponse response = mapFromCourseToCourseResponse(course);
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            log.error("Course not found");
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("An error occurred while updating the course");
            // put the error message in the response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<?> deleteCourse(String courseId, Long instructorId) {
        try {
            Course course = courseRepository.findById(Long.parseLong(courseId)).orElseThrow();

            if (!course.getInstructorId().equals(instructorId)) {
                log.error("User is not authorized to delete this course");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            courseRepository.delete(course);
            log.info("Course deleted successfully");
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            log.error("Course not found");
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("An error occurred while deleting the course");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // @KafkaListener(topics = "content-updates", groupId =
    // "content-course-communication-group")
    // public void consume(String message) {
    // log.info("Consumed message: " + message);
    // }

}

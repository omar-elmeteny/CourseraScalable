package com.bugbusters.course.service;

import com.bugbusters.course.dto.CourseCreateRequest;
import com.bugbusters.course.dto.CourseResponse;
import com.bugbusters.course.models.Course;
import com.bugbusters.course.repository.CourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@AllArgsConstructor
@lombok.extern.slf4j.Slf4j
public class CourseService {

    private final CourseRepository courseRepository;
    private final RestTemplate restTemplate;
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

    public CourseResponse mapFromCourseToCourseResponse(Course course) {
        return new CourseResponse(course.getId(), course.getName(), course.getDescription(), course.getInstructorId(), course.getPrice(), course.getCategories(), course.getStatus());
    }
}

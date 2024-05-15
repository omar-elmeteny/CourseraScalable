package com.bugbusters.course.dto.Course;

import com.bugbusters.course.models.course.CourseCategory;
import com.bugbusters.course.models.course.CourseStatus;

import java.io.Serializable;
import java.util.Set;

public record CourseResponse(Long id, String name, String description, Long instructorId, double price, double rating,
                Set<CourseCategory> categories, CourseStatus status) implements Serializable {
}

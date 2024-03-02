package com.bugbusters.course.dto;

import com.bugbusters.course.models.CourseCategory;
import com.bugbusters.course.models.CourseStatus;

import java.util.Set;

public record CourseResponse(Long id, String name, String description, Long instructorId, double price, Set<CourseCategory> categories, CourseStatus status) {
}

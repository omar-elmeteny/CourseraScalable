package com.bugbusters.course.dto;

import com.bugbusters.course.models.CourseCategory;
import java.util.Set;

public record CourseCreateRequest (String name, String description, Long instructorId, double price, Set<CourseCategory> categories) {
}

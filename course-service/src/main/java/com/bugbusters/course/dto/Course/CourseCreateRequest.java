package com.bugbusters.course.dto.Course;

import com.bugbusters.course.models.course.CourseCategory;
import java.util.Set;

public record CourseCreateRequest(String name, String description, double price,
        Set<CourseCategory> categories) {
}

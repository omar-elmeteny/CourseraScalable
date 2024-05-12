package com.bugbusters.course.dto.Course;

import com.bugbusters.course.models.course.CourseCategory;
import com.bugbusters.course.models.course.CourseStatus;
import java.util.Optional;

import java.util.Set;

public record CourseUpdateRequest(Optional<String> name, Optional<String> description, Optional<Double> price,
        Optional<Set<CourseCategory>> categories,
        Optional<CourseStatus> status) {

}

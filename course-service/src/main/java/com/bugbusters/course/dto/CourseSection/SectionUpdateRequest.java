package com.bugbusters.course.dto.CourseSection;

import java.util.Optional;

public record SectionUpdateRequest(Optional<String> title, Optional<String> description,
        Optional<Integer> orderNumber) {

}

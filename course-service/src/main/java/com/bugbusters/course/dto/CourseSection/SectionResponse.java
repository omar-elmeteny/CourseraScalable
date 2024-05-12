package com.bugbusters.course.dto.CourseSection;

import java.util.UUID;

public record SectionResponse(UUID id, String title, String description, Integer orderNumber) {
}

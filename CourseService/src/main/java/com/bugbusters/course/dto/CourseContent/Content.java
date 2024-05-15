package com.bugbusters.course.dto.CourseContent;

import java.util.UUID;

public record Content(UUID id, Long duration, String title, Integer orderNumber) {

}

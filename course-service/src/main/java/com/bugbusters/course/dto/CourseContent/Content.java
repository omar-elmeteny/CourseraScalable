package com.bugbusters.course.dto.CourseContent;

import java.util.UUID;
import java.time.Duration;

public record Content(Long id, Duration duration, String title, UUID multimediaId, Integer orderNumber) {

}

package com.bugbusters.course.dto;
import java.util.Date;
public record ReviewResponse (Long id, Long courseId, Long userId, String comment, Date date) {
}

package com.bugbusters.course.dto.CourseSection;

import java.util.List;
import com.bugbusters.course.dto.CourseContent.Content;

public record DetailedSectionResponse(SectionResponse section, List<Content> content) {
}

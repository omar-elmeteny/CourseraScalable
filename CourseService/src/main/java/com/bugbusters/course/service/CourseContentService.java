package com.bugbusters.course.service;

import com.bugbusters.course.kafka_requests.AddContentRequest;
import com.bugbusters.course.models.course_content.CourseContent;
import com.bugbusters.course.models.course_section.CourseSection;
import com.bugbusters.course.repository.CourseContentRepository;
import com.bugbusters.course.repository.CourseSectionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@AllArgsConstructor
@lombok.extern.slf4j.Slf4j
public class CourseContentService {

    private final CourseContentRepository courseContentRepository;
    private final CourseSectionRepository courseSectionRepository;

    public void createContent(AddContentRequest request) {
        Optional<CourseSection> courseSection = courseSectionRepository.findById(request.getSectionId());
        CourseContent courseContent = CourseContent.builder()
                .id(request.getMultimediaId())
                .section(courseSection.orElseThrow())
                .title(request.getTitle())
                .type(request.getType())
                .orderNumber(request.getOrderNumber())
                .duration(request.getDuration())
                .build();
        courseContentRepository.save(courseContent);
        log.info("Course content created successfully");
    }

}

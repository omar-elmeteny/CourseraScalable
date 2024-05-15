package com.bugbusters.course.service;

import com.bugbusters.course.dto.CourseSection.DetailedSectionResponse;
import com.bugbusters.course.dto.CourseSection.SectionCreateRequest;
import com.bugbusters.course.dto.CourseSection.SectionResponse;
import com.bugbusters.course.dto.CourseSection.SectionUpdateRequest;
import com.bugbusters.course.kafka_config.CommandNames;
import com.bugbusters.course.kafka_requests.DeleteSectionContentRequest;
import com.bugbusters.course.models.course.Course;
import com.bugbusters.course.models.course_content.CourseContent;
import com.bugbusters.course.models.course_section.CourseSection;
import com.bugbusters.course.repository.CourseRepository;
import com.bugbusters.course.repository.CourseSectionRepository;
import com.bugbusters.course.dto.CourseContent.Content;
import java.util.Optional;
import java.util.UUID;

import com.guctechie.messages.exceptions.MessageQueueException;
import com.guctechie.messages.services.CommandDispatcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;
import com.bugbusters.course.repository.CourseContentRepository;

@Service
@AllArgsConstructor
@lombok.extern.slf4j.Slf4j
public class CourseSectionService {

    private final CourseSectionRepository courseSectionRepository;
    private final CourseRepository courseRepository;
    private final CourseContentRepository courseContentRepository;
    private final CommandDispatcher commandDispatcher;

    public ResponseEntity<SectionResponse> createCourseSection(SectionCreateRequest request, Long courseId,
            Long instructorId) {
        try {
            Optional<Course> course = courseRepository.findById(courseId);

            if (course.isEmpty()) {
                log.error("Course not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            System.out.println(course.get().getInstructorId() + " " + instructorId);
            if (!course.get().getInstructorId().equals(instructorId)) {
                log.error("User is not authorized to create a section for this course");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            CourseSection courseSection = CourseSection.builder()
                    .id(UUID.randomUUID())
                    .title(request.title())
                    .description(request.description())
                    .course(course.orElseThrow())
                    .orderNumber(request.orderNumber())
                    .build();
            courseSectionRepository.save(courseSection);
            log.info("Course section created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(mapFromCourseSectionToSectionResponse(courseSection));
        } catch (Exception e) {
            log.error("Error creating course section", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public List<SectionResponse> getAllCourseSections(Long courseId) {
        List<CourseSection> courseSections = courseSectionRepository.findAllByCourseId(courseId);
        return courseSections.stream().map(this::mapFromCourseSectionToSectionResponse).toList();
    }

    public SectionResponse mapFromCourseSectionToSectionResponse(CourseSection courseSection) {
        return new SectionResponse(courseSection.getId(), courseSection.getTitle(), courseSection.getDescription(),
                courseSection.getOrderNumber());
    }

    public Content mapFromCourseContentToContent(CourseContent courseContent) {
        return new Content(courseContent.getId(), courseContent.getDuration(), courseContent.getTitle(),
                courseContent.getOrderNumber());
    }

    public ResponseEntity<DetailedSectionResponse> getCourseSection(Long courseId, UUID sectionId) {
        try {
            CourseSection courseSection = courseSectionRepository.findById(sectionId).orElseThrow();
            SectionResponse sectionResponse = mapFromCourseSectionToSectionResponse(courseSection);
            List<CourseContent> courseContents = courseContentRepository.findAllBySectionId(sectionId);
            List<Content> contents = courseContents.stream().map(this::mapFromCourseContentToContent).toList();
            return ResponseEntity.ok(new DetailedSectionResponse(sectionResponse, contents));
        } catch (NoSuchElementException e) {
            log.error("Course section not found");
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("An error occurred while getting the course section"
                    + " with id: " + sectionId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<SectionResponse> updateCourseSection(SectionUpdateRequest request, Long courseId,
            Long instructorId,
            UUID sectionId) {
        try {
            Optional<Course> course = courseRepository.findById(courseId);
            if (course.isEmpty()) {
                log.error("Course not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            if (!course.get().getInstructorId().equals(instructorId)) {
                log.error("User is not authorized to update a section for this course");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            Optional<CourseSection> courseSection = courseSectionRepository.findById(sectionId);
            if (courseSection.isEmpty()) {
                log.error("Course section not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            CourseSection updatedCourseSection = courseSection.get();
            updatedCourseSection.setTitle(request.title().orElse(updatedCourseSection.getTitle()));
            updatedCourseSection.setDescription(request.description().orElse(updatedCourseSection.getDescription()));
            updatedCourseSection.setOrderNumber(request.orderNumber().orElse(updatedCourseSection.getOrderNumber()));
            courseSectionRepository.save(updatedCourseSection);
            log.info("Course section updated successfully");
            return ResponseEntity.ok(mapFromCourseSectionToSectionResponse(updatedCourseSection));
        } catch (Exception e) {
            log.error("An error occurred while updating the course section"
                    + " with id: " + sectionId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<Void> deleteCourseSection(Long courseId, Long instructorId, UUID sectionId) {
        try {
            Optional<Course> course = courseRepository.findById(courseId);
            if (course.isEmpty()) {
                log.error("Course not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            if (!course.get().getInstructorId().equals(instructorId)) {
                log.error("User is not authorized to delete a section for this course");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            Optional<CourseSection> courseSection = courseSectionRepository.findById(sectionId);
            List<CourseContent> sectionContents = courseContentRepository.findAllBySectionId(sectionId);
            if (courseSection.isEmpty()) {
                log.error("Course section not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            courseSectionRepository.delete(courseSection.get());
            log.info("Course section deleted successfully");
            deleteSectionContents(sectionContents);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("An error occurred while deleting the course section"
                    + " with id: " + sectionId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private void deleteSectionContents(List<CourseContent> sectionContents) throws MessageQueueException {
        List<UUID> contentIds = sectionContents.stream().map(CourseContent::getId).toList();
        this.commandDispatcher.sendAsyncCommand(CommandNames.DELETE_SECTION_CONTENT_COMMAND,
                DeleteSectionContentRequest.builder().contentIds(new ArrayList<>(contentIds)).build());
    }

}

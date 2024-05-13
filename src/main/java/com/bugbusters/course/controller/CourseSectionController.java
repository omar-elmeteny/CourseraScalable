package com.bugbusters.course.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bugbusters.course.dto.CourseSection.DetailedSectionResponse;
import com.bugbusters.course.dto.CourseSection.SectionCreateRequest;
import com.bugbusters.course.dto.CourseSection.SectionResponse;
import com.bugbusters.course.dto.CourseSection.SectionUpdateRequest;
import com.bugbusters.course.messages.exceptions.MessageQueueException;
import com.bugbusters.course.service.CourseSectionService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/course")
@EnableCaching
public class CourseSectionController {

    @Autowired
    private final CourseSectionService courseSectionService;

    @PostMapping("/{courseId}/section")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SectionResponse> createCourseSection(@RequestBody SectionCreateRequest request,
            @PathVariable(name = "courseId") Long courseId) {
        log.info("Creating course section, title: {}", request.title());
        // we may need to validate if the user is an instructor
        Long instructorId = 1L; // will be gotten from the bearer token
        return courseSectionService.createCourseSection(request, courseId, instructorId);
    }

    @GetMapping("/{courseId}/section")
    @ResponseStatus(HttpStatus.OK)
    public List<SectionResponse> getAllCourseSections(@PathVariable(name = "courseId") Long courseId) {
        log.info("Getting all course sections");
        return courseSectionService.getAllCourseSections(courseId);
    }

    @GetMapping("/{courseId}/section/{sectionId}")
    @ResponseStatus(HttpStatus.OK)
    @Cacheable(key = "#courseId", value = "Section")
    public ResponseEntity<DetailedSectionResponse> getCourseSection(@PathVariable(name = "courseId") Long courseId,
            @PathVariable(name = "sectionId") UUID sectionId) {
        log.info("Getting course section with id: {}", sectionId);
        return courseSectionService.getCourseSection(courseId, sectionId);
    }

    @PutMapping("/{courseId}/section/{sectionId}")
    @CachePut(key = "#courseId")
    public ResponseEntity<SectionResponse> updateCourseSection(@RequestBody SectionUpdateRequest request,
            @PathVariable(name = "courseId") Long courseId, @PathVariable(name = "sectionId") UUID sectionId) {
        log.info("Updating course section with id: {}", sectionId);
        // we may need to validate if the user is an instructor
        Long instructorId = 1L; // will be gotten from the bearer token
        return courseSectionService.updateCourseSection(request, courseId, instructorId, sectionId);
    }

    @DeleteMapping("/{courseId}/section/{sectionId}")
    @CacheEvict(key = "#courseId")
    public ResponseEntity<Void> deleteCourseSection(@PathVariable(name = "courseId") Long courseId,
            @PathVariable(name = "sectionId") UUID sectionId) throws MessageQueueException {
        log.info("Deleting course section with id: {}", sectionId);
        // we may need to validate if the user is an instructor
        Long instructorId = 1L; // will be gotten from the bearer token
        return courseSectionService.deleteCourseSection(courseId, instructorId, sectionId);
    }

}

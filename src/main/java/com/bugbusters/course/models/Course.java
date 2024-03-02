package com.bugbusters.course.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.Duration;
import java.util.Set;

@Entity
@Table(name = "course")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long instructorId;

    @Column(nullable = false)
    double price;

    // it will be calculated based on the course's reviews
    double rating;

    // it will be calculated based on the course's content
    private Duration duration;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private CourseStatus status = CourseStatus.Draft;

    @Column(nullable = false)
    @ElementCollection(targetClass = CourseCategory.class)
    @Enumerated(EnumType.STRING)
    private Set<CourseCategory> categories;
}

package com.bugbusters.course.models.course;

import com.bugbusters.course.models.course_review.CourseReview;
import com.bugbusters.course.models.course_section.CourseSection;
import com.bugbusters.course.models.course_content.CourseContent;
import com.bugbusters.course.models.course_enrollment.CourseEnrollment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.Duration;
import java.util.Set;
import java.util.UUID;

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
    private double price;

    // it will be calculated based on the course's reviews
    private double rating;

    // it will be calculated based on the course's content
    private Long duration;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private CourseStatus status = CourseStatus.Draft;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<CourseCategory> categories;

    @JsonIgnore
    @OneToMany(mappedBy = "course")
    private Set<CourseReview> reviews;

    @JsonIgnore
    @OneToMany(mappedBy = "course")
    private Set<CourseSection> sections;

    @JsonIgnore
    @OneToMany(mappedBy = "course")
    private Set<CourseEnrollment> enrollments;

    // @Column(nullable = false)
    private UUID certificateId;

    public static Course fromString(String courseString) {
        String[] parts = courseString.split(",");
        return Course.builder()
                .id(Long.parseLong(parts[0]))
                .name(parts[1])
                .description(parts[2])
                .instructorId(Long.parseLong(parts[3]))
                .price(Double.parseDouble(parts[4]))
                .categories(Set.of(CourseCategory.valueOf(parts[5])))
                .build();
    }

    public String toString() {
        return String.join(",", id.toString(), name, description, instructorId.toString(), Double.toString(price),
                categories.toString());
    }
}

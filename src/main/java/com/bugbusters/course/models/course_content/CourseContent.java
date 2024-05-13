package com.bugbusters.course.models.course_content;

import com.bugbusters.course.enums.ContentType;
import com.bugbusters.course.models.course_section.CourseSection;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.UUID;

@Entity(name = "course_content")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseContent {

    @Id
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "section_id", nullable = false, referencedColumnName = "id")
    private CourseSection section;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long duration;

    @Column(nullable = false)
    private Integer orderNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ContentType type;

}

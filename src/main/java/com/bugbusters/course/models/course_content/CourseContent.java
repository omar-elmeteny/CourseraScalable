package com.bugbusters.course.models.course_content;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "section_id", nullable = false, referencedColumnName = "id")
    private CourseSection section;

    @Column(nullable = false)
    private Duration duration;

    @Column(nullable = false)
    private Integer orderNumber;

    @Column(nullable = false)
    private UUID multimediaId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ContentType type;

}

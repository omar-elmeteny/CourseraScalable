package com.bugbusters.course.models.progress;

import com.bugbusters.course.models.course_enrollment.CourseEnrollment;
import com.bugbusters.course.models.course_section.CourseSection;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "progress")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "section_id", nullable = false, referencedColumnName = "id")
    private CourseSection section;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "enrollment_id", nullable = false, referencedColumnName = "id")
    private CourseEnrollment enrollment;

    @Column(nullable = false)
    private double progress;
}

package com.bugbusters.course.models.grade;

import com.bugbusters.course.models.course_enrollment.CourseEnrollment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity(name = "grade")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "enrollment_id", nullable = false, referencedColumnName = "id")
    private CourseEnrollment enrollment;

    @Column(nullable = false)
    private double grade;

    @Column(nullable = false)
    private UUID contentId;

}

package com.bugbusters.course.models.course_enrollment;

import com.bugbusters.course.models.course.Course;
import com.bugbusters.course.models.course_section.CourseSection;
import com.bugbusters.course.models.grade.Grade;
import com.bugbusters.course.models.progress.Progress;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "course_enrollment")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id", nullable = false, referencedColumnName = "id")
    private Course course;

    @JsonIgnore
    @OneToMany(mappedBy = "enrollment")
    private Set<Progress> progresses;

    @JsonIgnore
    @OneToMany(mappedBy = "enrollment")
    private Set<Grade> grades;

    private UUID certificateId;
}

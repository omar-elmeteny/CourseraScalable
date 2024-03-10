package com.bugbusters.course.models.course_review;

import com.bugbusters.course.models.course.Course;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity(name = "course_review")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id", nullable = false, referencedColumnName = "id")
    private Course course;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String comment;

    private Date date;
}

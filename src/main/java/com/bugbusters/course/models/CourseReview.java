package com.bugbusters.course.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity(name = "CourseReview")
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

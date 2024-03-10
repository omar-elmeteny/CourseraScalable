package com.bugbusters.course.models.course_section;

import com.bugbusters.course.models.course.Course;
import com.bugbusters.course.models.course_content.CourseContent;
import com.bugbusters.course.models.progress.Progress;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "course_section")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id", nullable = false, referencedColumnName = "id")
    private Course course;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer orderNumber;

    @JsonIgnore
    @OneToMany(mappedBy = "section")
    private Set<CourseContent> courseContents;

    @JsonIgnore
    @OneToMany(mappedBy = "section")
    private Set<Progress> progresses;
}

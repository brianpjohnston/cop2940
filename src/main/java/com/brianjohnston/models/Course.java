package com.brianjohnston.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

/**
 * StudentLabTracker
 * 6/8/16 12:55 AM
 */
@Data
@ToString(exclude = "instructor")
@NoArgsConstructor
@Entity
@NamedQueries(
        @NamedQuery(
                name = "numberOfCourses",
                query = "select count(*) from Course"
        )
)
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @ManyToOne
    private Instructor instructor;
    @ManyToOne(cascade = CascadeType.ALL)
    private Location location;
}

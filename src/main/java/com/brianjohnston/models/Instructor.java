package com.brianjohnston.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * StudentLabTracker
 * 6/7/16 11:33 PM
 */
@Data
@NoArgsConstructor
@Entity
public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer      id;
    @Column
    @NotBlank
    private String       firstName;
    @Column
    @NotBlank
    private String       lastName;
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "instructor")
    List<Course> courses;
}

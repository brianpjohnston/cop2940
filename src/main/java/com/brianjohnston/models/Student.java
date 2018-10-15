package com.brianjohnston.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;


/**
 * StudentLabTracker
 * 6/7/16 3:54 PM
 */
@Entity
@NoArgsConstructor
@Data
@NamedQueries(
        {
                @NamedQuery(name = "findStudentByName",
                        query = "from Student where firstName = :first and lastName = :last"),
                @NamedQuery(
                        name = "numberOfStudents",
                        query = "select count(*) from Student "
                )
        }
)
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    @NotBlank
    private String firstName;
    @Column
    @NotBlank
    private String lastName;
}

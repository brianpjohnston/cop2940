package com.brianjohnston.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * StudentLabTracker
 * 6/7/16 11:37 PM
 */
@Data
@NoArgsConstructor
@Entity
@NamedQueries(
        {
                @NamedQuery(name = "getLoggedInEvents", query = "from LabEvent where instructor.id = :id and leave is null"),
                @NamedQuery(name = "getEventsByInstructor", query = "from LabEvent where instructor.id = :id and leave is not null ")
        }
)

public class LabEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer    id;
    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    @NotNull
    private Date       enter;
    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date       leave;
    @OneToOne(cascade=CascadeType.ALL)
    @NotNull
    private Student    student;
    @OneToOne(cascade=CascadeType.ALL)
    @NotNull
    private Instructor instructor;
    @OneToOne(cascade=CascadeType.ALL)
    @NotNull
    private Course     course;
}

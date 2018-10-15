package com.brianjohnston.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * StudentLabTracker
 * 6/8/16 1:23 AM
 */
@Entity
@NoArgsConstructor
@Data
@NamedQueries(
        {
                @NamedQuery(
                        name = "findById",
                        query = "FROM Location"
                ),
                @NamedQuery(
                        name = "numberOfLocations",
                        query = "select count(*) from Location "
                )
        }
)
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    @NotBlank
    private String  campus;
    @Column
    @NotBlank
    private String  building;
    @Column
    @NotNull
    private Integer room;
}

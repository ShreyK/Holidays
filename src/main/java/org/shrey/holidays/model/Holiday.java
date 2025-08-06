package org.shrey.holidays.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;


@Entity
@Data
@Table(name = "holiday")
public class Holiday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country", columnDefinition = "VARCHAR(50)")
    private String country;

    @Temporal(TemporalType.DATE)
    @Column(name = "date", columnDefinition = "DATE")
    private LocalDate date;

    @Column(name = "description", nullable = true, columnDefinition = "VARCHAR(100)")
    private String description;
}

package org.shrey.holidays.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayDTO {
    private String country;
    private LocalDate date;
    private String description;
}

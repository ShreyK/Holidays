package org.shrey.holidays.repository;

import org.shrey.holidays.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    List<Holiday> findHolidaysByCountry(String country);
    List<Holiday> findHolidaysByCountryAndDateBetween(String country, LocalDate first, LocalDate second);
    Holiday findHolidayByCountryAndDate(String country, LocalDate date);
}

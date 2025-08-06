package org.shrey.holidays.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shrey.holidays.model.Holiday;
import org.shrey.holidays.model.HolidayDTO;
import org.shrey.holidays.service.HolidayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/holiday")
@Slf4j
public class HolidayController {

    final HolidayService holidayService;

    @GetMapping("/{country}")
    public ResponseEntity<List<Holiday>> getAllHolidays(@PathVariable String country) {
        log.info("Fetching all holidays for {}", country);
        return ResponseEntity.ok(holidayService.getHolidays(country));
    }

    @GetMapping("/{country}/{year}")
    public ResponseEntity<List<Holiday>> getAllHolidaysForYear(@PathVariable int year, @PathVariable String country) {
        log.info("Fetching all holidays for {} for year {}", country, year);
        return ResponseEntity.ok(holidayService.getHolidaysForYear(year, country));
    }

    @GetMapping("/{country}/remaining")
    public ResponseEntity<List<Holiday>> getHolidaysRemainingInCurrentYear(@PathVariable String country) {
        log.info("Fetching all holidays for {} remaining for current year {}", country, LocalDate.now().getYear());
        return ResponseEntity.ok(holidayService.getHolidaysRemaining(country));
    }

    @PostMapping
    public ResponseEntity<Void> saveHoliday(@RequestBody HolidayDTO holidayDTO) {
        log.info("Saving holiday for country {}", holidayDTO.getCountry());
        holidayService.createHoliday(holidayDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/upload")
    public ResponseEntity<Void> uploadHolidays(@RequestParam("file") MultipartFile file) {
        holidayService.uploadHolidays(file);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateHolidays(@PathVariable Long id, @RequestBody HolidayDTO holidayDTO) {
        holidayService.updateHoliday(id, holidayDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHolidays(@PathVariable Long id) {
        holidayService.deleteHoliday(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

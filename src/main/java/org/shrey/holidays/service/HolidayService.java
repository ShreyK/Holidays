package org.shrey.holidays.service;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shrey.holidays.exception.FileUploadException;
import org.shrey.holidays.exception.NoHolidayFoundException;
import org.shrey.holidays.model.Holiday;
import org.shrey.holidays.model.HolidayDTO;
import org.shrey.holidays.repository.HolidayRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class HolidayService {

    final HolidayRepository holidayRepository;

    public List<Holiday> getHolidays(String country) {
        return holidayRepository.findHolidaysByCountry(country);
    }

    public List<Holiday> getHolidaysForYear(int year, String country) {
        LocalDate firstDate = LocalDate.of(year, 1, 1);
        LocalDate secondDate = LocalDate.of(year, 12, 31);
        return holidayRepository.findHolidaysByCountryAndDateBetween(country, firstDate, secondDate);
    }

    public List<Holiday> getHolidaysRemaining(String country) {
        LocalDate firstDate = LocalDate.now();
        LocalDate secondDate = LocalDate.of(firstDate.getYear(), 12, 31);
        return holidayRepository.findHolidaysByCountryAndDateBetween(country, firstDate, secondDate);
    }

    @Transactional
    public void createHoliday(HolidayDTO holidayDTO) {
        Holiday holiday = new Holiday();
        holiday.setCountry(holidayDTO.getCountry());
        holiday.setDate(holidayDTO.getDate());
        holiday.setDescription(holidayDTO.getDescription());
        holidayRepository.save(holiday);
    }

    @Transactional
    public void updateHoliday(Long id, HolidayDTO holidayDTO) throws NoHolidayFoundException {
        if (!holidayRepository.existsById(id)) {
            throw new NoHolidayFoundException();
        }
        holidayRepository.findById(id).map(holiday -> {
            holiday.setCountry(holidayDTO.getCountry());
            holiday.setDate(holidayDTO.getDate());
            holiday.setDescription(holidayDTO.getDescription());
            return holidayRepository.save(holiday);
        });
    }

    @Transactional
    public void deleteHoliday(Long id) {
        if (!holidayRepository.existsById(id)) {
            throw new NoHolidayFoundException();
        }
        holidayRepository.deleteById(id);
    }

    @Transactional
    public void uploadHolidays(MultipartFile file) throws FileUploadException {
        CsvMapper mapper = new CsvMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            InputStream stream = file.getInputStream();
            CsvSchema schema = mapper.schemaFor(HolidayDTO.class).withHeader().withColumnReordering(true);
            ObjectReader reader = mapper.readerFor(HolidayDTO.class).with(schema);
            List<HolidayDTO> holidays = reader.<HolidayDTO>readValues(stream).readAll();
            holidays.forEach(holidayDTO -> {
                Holiday holidayExists = holidayRepository.findHolidayByCountryAndDate(holidayDTO.getCountry(), holidayDTO.getDate());
                if (holidayExists != null) {
                    holidayExists.setDescription(holidayDTO.getDescription());
                    log.info("Updating holiday from file {}", holidayExists);
                    holidayRepository.save(holidayExists);
                } else {
                    Holiday holiday = new Holiday();
                    holiday.setCountry(holidayDTO.getCountry());
                    holiday.setDate(holidayDTO.getDate());
                    holiday.setDescription(holidayDTO.getDescription());
                    log.info("Saving holiday from file {}", holiday);
                    holidayRepository.save(holiday);
                }
            });
        } catch (Exception e) {
            throw new FileUploadException(e.getMessage());
        }
    }
}

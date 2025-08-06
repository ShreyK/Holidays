package org.shrey.holidays.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.shrey.holidays.exception.FileUploadException;
import org.shrey.holidays.exception.NoHolidayFoundException;
import org.shrey.holidays.model.Holiday;
import org.shrey.holidays.model.HolidayDTO;
import org.shrey.holidays.repository.HolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
@Import(HolidayService.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class HolidayServiceTest {

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private HolidayRepository holidayRepository;

    @Test
    void createHoliday() {
        HolidayDTO holidayDTO = new HolidayDTO();
        holidayDTO.setCountry("US");
        holidayDTO.setDescription("Independance Day");
        holidayDTO.setDate(LocalDate.parse("2018-04-06"));

        holidayService.createHoliday(holidayDTO);
        List<Holiday> holidays = holidayRepository.findHolidaysByCountry("US");
        Assertions.assertThat(holidays).hasSize(1);
        Assertions.assertThat(holidays.get(0).getDate().getYear()).isEqualTo(2018);
        Assertions.assertThat(holidays.get(0).getDate().getMonth().getValue()).isEqualTo(4);
        Assertions.assertThat(holidays.get(0).getDate().getDayOfMonth()).isEqualTo(6);
    }

    @Test
    void updateHoliday() {
        HolidayDTO holidayDTO = new HolidayDTO();
        holidayDTO.setCountry("US");
        holidayDTO.setDescription("Independance Day");
        holidayDTO.setDate(LocalDate.parse("2018-07-01"));

        holidayService.createHoliday(holidayDTO);
        List<Holiday> holidays = holidayRepository.findHolidaysByCountry("US");
        Assertions.assertThat(holidays).hasSize(1);

        holidayDTO.setCountry("CA");
        holidayService.updateHoliday(1L, holidayDTO);

        holidays = holidayRepository.findHolidaysByCountry("CA");
        Assertions.assertThat(holidays).hasSize(1);
        holidays = holidayRepository.findHolidaysByCountry("US");
        Assertions.assertThat(holidays).hasSize(0);

        Assertions.assertThatThrownBy(() -> holidayService.updateHoliday(5L, holidayDTO)).isInstanceOf(NoHolidayFoundException.class);
    }

    @Test
    void getHolidays() {
        // Shouldn't be shown in service request as holiday is before Date.now()
        HolidayDTO holidayDTO = new HolidayDTO();
        holidayDTO.setCountry("US");
        holidayDTO.setDescription("Independance Day");
        holidayDTO.setDate(LocalDate.parse("2025-04-06"));
        holidayService.createHoliday(holidayDTO);

        // Should be shown in service request
        holidayDTO = new HolidayDTO();
        holidayDTO.setCountry("US");
        holidayDTO.setDescription("Christmas Day");
        holidayDTO.setDate(LocalDate.parse("2025-12-25"));
        holidayService.createHoliday(holidayDTO);

        // Should be shown in service request
        holidayDTO = new HolidayDTO();
        holidayDTO.setCountry("US");
        holidayDTO.setDescription("Boxing Day");
        holidayDTO.setDate(LocalDate.parse("2025-11-20"));
        holidayService.createHoliday(holidayDTO);

        // Shouldn't be shown in service request as holiday is next year
        holidayDTO = new HolidayDTO();
        holidayDTO.setCountry("US");
        holidayDTO.setDescription("Boxing Day");
        holidayDTO.setDate(LocalDate.parse("2026-11-20"));
        holidayService.createHoliday(holidayDTO);

        List<Holiday> holidays = holidayService.getHolidaysRemaining("US");
        Assertions.assertThat(holidays).hasSize(2);
        Assertions.assertThat(holidays.get(0).getDate().getDayOfMonth()).isEqualTo(25);
        Assertions.assertThat(holidays.get(1).getDate().getDayOfMonth()).isEqualTo(20);

        holidays = holidayService.getHolidays("US");
        Assertions.assertThat(holidays).hasSize(4);
    }

    @Test
    void getHolidaysForCurrentYear() {
        // Shouldn't be shown in service request as holiday is before Date.now()
        HolidayDTO holidayDTO = new HolidayDTO();
        holidayDTO.setCountry("US");
        holidayDTO.setDescription("Independance Day");
        holidayDTO.setDate(LocalDate.parse("2025-07-04"));
        holidayService.createHoliday(holidayDTO);

        // Should be shown in service request
        holidayDTO = new HolidayDTO();
        holidayDTO.setCountry("US");
        holidayDTO.setDescription("Christmas Day");
        holidayDTO.setDate(LocalDate.parse("2025-12-25"));
        holidayService.createHoliday(holidayDTO);

        // Should be shown in service request
        holidayDTO = new HolidayDTO();
        holidayDTO.setCountry("US");
        holidayDTO.setDescription("Boxing Day");
        holidayDTO.setDate(LocalDate.parse("2025-11-20"));
        holidayService.createHoliday(holidayDTO);

        // Shouldn't be shown in service request as holiday is next year
        holidayDTO = new HolidayDTO();
        holidayDTO.setCountry("US");
        holidayDTO.setDescription("Boxing Day");
        holidayDTO.setDate(LocalDate.parse("2026-11-20"));
        holidayService.createHoliday(holidayDTO);

        List<Holiday> holidays = holidayService.getHolidaysForYear(2025, "US");
        Assertions.assertThat(holidays).hasSize(3);
        Assertions.assertThat(holidays.get(0).getDate().getDayOfMonth()).isEqualTo(4);
        Assertions.assertThat(holidays.get(1).getDate().getDayOfMonth()).isEqualTo(25);
        Assertions.assertThat(holidays.get(2).getDate().getDayOfMonth()).isEqualTo(20);
    }

    @Test
    void deleteHolidayTest() {
        // Shouldn't be shown in service request as holiday is before Date.now()
        HolidayDTO holidayDTO = new HolidayDTO();
        holidayDTO.setCountry("US");
        holidayDTO.setDescription("Independance Day");
        holidayDTO.setDate(LocalDate.parse("2025-07-04"));
        holidayService.createHoliday(holidayDTO);

        // Should be shown in service request
        holidayDTO = new HolidayDTO();
        holidayDTO.setCountry("US");
        holidayDTO.setDescription("Christmas Day");
        holidayDTO.setDate(LocalDate.parse("2025-12-25"));
        holidayService.createHoliday(holidayDTO);

        // Should be shown in service request
        holidayDTO = new HolidayDTO();
        holidayDTO.setCountry("US");
        holidayDTO.setDescription("Boxing Day");
        holidayDTO.setDate(LocalDate.parse("2025-11-20"));
        holidayService.createHoliday(holidayDTO);

        // Shouldn't be shown in service request as holiday is next year
        holidayDTO = new HolidayDTO();
        holidayDTO.setCountry("US");
        holidayDTO.setDescription("Boxing Day");
        holidayDTO.setDate(LocalDate.parse("2026-11-20"));
        holidayService.createHoliday(holidayDTO);

        List<Holiday> holidays = holidayService.getHolidaysForYear(2025, "US");
        Assertions.assertThat(holidays).hasSize(3);
        Assertions.assertThat(holidays.get(0).getDate().getDayOfMonth()).isEqualTo(4);
        Assertions.assertThat(holidays.get(1).getDate().getDayOfMonth()).isEqualTo(25);
        Assertions.assertThat(holidays.get(2).getDate().getDayOfMonth()).isEqualTo(20);

        // delete first holiday
        holidayService.deleteHoliday(1L);

        holidays = holidayService.getHolidaysForYear(2025, "US");
        Assertions.assertThat(holidays).hasSize(2);
        Assertions.assertThat(holidays.get(0).getDate().getDayOfMonth()).isEqualTo(25);
        Assertions.assertThat(holidays.get(1).getDate().getDayOfMonth()).isEqualTo(20);

        Assertions.assertThatThrownBy(() -> holidayService.deleteHoliday(6L)).isInstanceOf(NoHolidayFoundException.class);
    }

    @Test
    void uploadHolidays() {
        try {
            // Updated by upload file
            HolidayDTO holidayDTO = new HolidayDTO();
            holidayDTO.setCountry("US");
            holidayDTO.setDescription("Independance");
            holidayDTO.setDate(LocalDate.parse("2025-07-04"));
            holidayService.createHoliday(holidayDTO);

            ClassPathResource file = new ClassPathResource("data.csv");
            MultipartFile mockMultipartFile = new MockMultipartFile("mockMultipartFile", file.getInputStream());
            holidayService.uploadHolidays(mockMultipartFile);

            List<Holiday> holidays = holidayRepository.findHolidaysByCountry("US");
            Assertions.assertThat(holidays).hasSize(3);
            holidays = holidayRepository.findHolidaysByCountry("CA");
            Assertions.assertThat(holidays).hasSize(4);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void uploadHolidaysFailure() {
        try {
            MultipartFile mockMultipartFile = null;
            Assertions.assertThatThrownBy(() -> holidayService.uploadHolidays(mockMultipartFile)).isInstanceOf(FileUploadException.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
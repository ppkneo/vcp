package com.example.demo.controller;

import com.example.demo.service.VacationPayCalculatorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;

@RestController
public class VacationPayCalculatorController {
    private final Logger LOGGER = LogManager.getLogger(VacationPayCalculatorController.class);
    private final VacationPayCalculatorService vacationPayCalculatorService;

    public VacationPayCalculatorController(VacationPayCalculatorService vacationPayCalculatorService) {
        this.vacationPayCalculatorService = vacationPayCalculatorService;
    }

    @GetMapping("/calculate")
    public BigDecimal calculatePay(@RequestParam(name = "averageSalary") BigDecimal averageSalary,
                                   @RequestParam(name = "vacationDays") Integer vacationDays,
                                   @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                   @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) throws DateTimeException {
        BigDecimal vacationPay;

        if (startDate != null && endDate != null) {
            if (startDate.isAfter(endDate)) {
                LOGGER.error("Start date {} should go before the end date {}", startDate, endDate);
                throw new DateTimeException("StartDate > EndDate");
            }
            vacationPay = vacationPayCalculatorService.calculateVacationPayWithDates(averageSalary, startDate, endDate);
        } else if (startDate != null) {
            vacationPay = vacationPayCalculatorService.calculateVacationPayWithStartDay(averageSalary, vacationDays, startDate);
        } else {
            vacationPay = vacationPayCalculatorService.calculateVacationPay(averageSalary, vacationDays);
        }
        return vacationPay;
    }


}
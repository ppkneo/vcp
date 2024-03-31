package com.example.demo.service.impl;

import com.example.demo.service.CalendarHolidaysService;
import com.example.demo.service.VacationPayCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class VacationPayCalculatorServiceRussian implements VacationPayCalculatorService {
    private final CalendarHolidaysService calendarHolidaysService;

    /**
     * Считает зарплаты за 1 рабочий день
     *
     * @param averageSalary зарплата за 12 месяцев
     * @return зарплата за рабочий день
     */
    private BigDecimal calculateSalaryPerDay(BigDecimal averageSalary) {
        final int monthsInYear = 12;
        final double avgWorkingDaysPerMonth = calendarHolidaysService.getAverageWorkingDaysPerMonth();
        return averageSalary
                .divide(java.math.BigDecimal.valueOf(monthsInYear), 2, RoundingMode.HALF_UP)
                .divide(java.math.BigDecimal.valueOf(avgWorkingDaysPerMonth), 2, RoundingMode.HALF_UP);
    }

    @Autowired
    public VacationPayCalculatorServiceRussian(final CalendarHolidaysService calendarHolidaysService) {
        this.calendarHolidaysService = calendarHolidaysService;
    }

    @Override
    public BigDecimal calculateVacationPayWithDates(BigDecimal averageSalary, LocalDate startDate, LocalDate endDate) {
        final BigDecimal salaryPerDay = calculateSalaryPerDay(averageSalary);
        final int vacationDaysWithHolidays = calendarHolidaysService.calculateVacationDaysWithHolidaysAndDates(
                startDate,
                endDate
        );
        return salaryPerDay.multiply(BigDecimal.valueOf(vacationDaysWithHolidays));
    }

    @Override
    public BigDecimal calculateVacationPayWithStartDay(BigDecimal averageSalary, Integer vacationDays, LocalDate startDate) {
        final BigDecimal salaryPerDay = calculateSalaryPerDay(averageSalary);
        final int vacationDaysWithHolidays = calendarHolidaysService.calculateVacationDaysWithHolidaysAndStartDay(
                startDate, vacationDays
        );
        return salaryPerDay.multiply(BigDecimal.valueOf(vacationDaysWithHolidays));
    }

    @Override
    public BigDecimal calculateVacationPay(BigDecimal averageSalary, Integer vacationDays) {
        final BigDecimal salaryPerDay = calculateSalaryPerDay(averageSalary);
        return salaryPerDay.multiply(BigDecimal.valueOf(vacationDays));
    }
}

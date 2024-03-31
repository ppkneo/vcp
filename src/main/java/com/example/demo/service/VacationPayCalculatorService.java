package com.example.demo.service;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface VacationPayCalculatorService {
    /**
     * Расчёт отпускных, когда известна дата начала и конца отпуска
     *
     * @param averageSalary зарплата за 12 месяцев
     * @param startDate     дата начала отпуска
     * @param endDate       дата конца отпуска
     * @return сумма отпускных
     */
    BigDecimal calculateVacationPayWithDates(BigDecimal averageSalary, LocalDate startDate, LocalDate endDate);

    /**
     * Расчёт отпускных, когда известна дата начала и кол-во дней отпуска
     *
     * @param averageSalary зарплата за 12 месяцев
     * @param vacationDays  кол-во отпускных дней
     * @param startDate     дата начала отпуска
     * @return сумма отпускных
     */
    BigDecimal calculateVacationPayWithStartDay(BigDecimal averageSalary, Integer vacationDays, LocalDate startDate);

    /**
     * Расчёт отпускных, когда известно только кол-во отпускных дней
     *
     * @param averageSalary зарплата за 12 месяцев
     * @param vacationDays  кол-во отпускных дней
     * @return сумма отпускных
     */
    BigDecimal calculateVacationPay(BigDecimal averageSalary, Integer vacationDays);
}

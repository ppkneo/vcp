package com.example.demo.service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;

public interface CalendarHolidaysService {
    /**
     * Возвращает на какой год настроен сервис
     * @return
     */
    int forYear();

    /**
     * Возвращает праздничные дни по месяцам
     * @return
     */
    Map<Month, List<Integer>> getHolidaysList();

    /**
     * Возвращает среднее количество рабочих дней в месяце
     * @return
     */
    double getAverageWorkingDaysPerMonth();

    /**
     * Считает кол-во рабочих дней в промежутке между датами начала и конца
     * @param startDate
     * @param endDate
     * @return
     */
    int calculateVacationDaysWithHolidaysAndDates(LocalDate startDate, LocalDate endDate);

    /**
     * Считает кол-во рабочих дней с начальной даты по кол-ву выходных дней
     * @param startDate
     * @param vacationDays
     * @return
     */
    int calculateVacationDaysWithHolidaysAndStartDay(LocalDate startDate, Integer vacationDays);
}

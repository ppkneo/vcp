package com.example.demo.service.impl;

import com.example.demo.service.CalendarHolidaysService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@ConfigurationProperties(prefix="calendar")
public class CalendarHolidaysServiceImpl implements CalendarHolidaysService, InitializingBean {
    @Value("#{${calendar.holidays}}")
    private Map<Integer, String> holidays;
    @Value("${calendar.forYear}")
    private int forYear;
    @Value("${calendar.averageWorkingDaysPerMonth}")
    private double averageWorkingDaysPerMonth;
    private final Map<Month, List<Integer>> normalizedHolidays = new HashMap<>();

    private void setHolidays(final Map<Integer, String> holidays) {
        this.holidays = holidays;
    }

    @Override
    public void afterPropertiesSet() {
        for (final Map.Entry<Integer, String> entry : holidays.entrySet()) {
            final Month month = Month.of(entry.getKey());
            final List<Integer> days = Arrays.stream(entry.getValue().split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            normalizedHolidays.put(month, days);
        }
    }

    @Override
    public int forYear() {
        return forYear;
    }

    @Override
    public Map<Month, List<Integer>> getHolidaysList() {
        return normalizedHolidays;
    }

    @Override
    public double getAverageWorkingDaysPerMonth() {
        return averageWorkingDaysPerMonth;
    }

    @Override
    public int calculateVacationDaysWithHolidaysAndDates(LocalDate safeStartDate,
                                                         LocalDate safeEndDate) {
        int vacationDaysWithHolidays = 0;
        final Map<Month, List<Integer>> holidayDays = getHolidaysList();

        safeEndDate = safeEndDate.plusDays(1); // включительно
        while (!safeStartDate.equals(safeEndDate)) {
            // подсчёт кол-ва отпускных дней с учётом праздников и выходных
            var dayOfWeek = safeStartDate.getDayOfWeek().getValue();

            if (dayOfWeek == 6 || dayOfWeek == 7) { // пропуск субботы и воскресенья
                safeStartDate = safeStartDate.plusDays(1);
                continue;
            }
            if (holidayDays.get(safeStartDate.getMonth()) // лист праздничных дней по текущему месяцу
                    .contains(safeStartDate.getDayOfMonth())) { // пропускаем праздники
                safeStartDate = safeStartDate.plusDays(1);
                continue;
            }
            vacationDaysWithHolidays++; // рабочий день ++
            safeStartDate = safeStartDate.plusDays(1);
        }

        return vacationDaysWithHolidays;
    }

    @Override
    public int calculateVacationDaysWithHolidaysAndStartDay(LocalDate startDate, Integer vacationDays) {
        int vacationDaysWithHolidays = 0;
        var holidayDays = getHolidaysList();

        while (vacationDays != 0) {
            // подсчёт кол-ва отпускных дней с учётом праздников и выходных
            var dayOfWeek = startDate.getDayOfWeek().getValue();

            if (dayOfWeek == 6 || dayOfWeek == 7) { // пропуск субботы и воскресенья
                vacationDays--;
                startDate = startDate.plusDays(1);
                continue;
            }
            if (holidayDays.get(startDate.getMonth()) // лист праздничных дней по текущему месяцу
                    .contains(startDate.getDayOfMonth())) { // пропускаем праздники
                vacationDays--;
                startDate = startDate.plusDays(1);
                continue;
            }
            vacationDaysWithHolidays++; // рабочий день ++
            vacationDays--;
            startDate = startDate.plusDays(1);
        }

        return vacationDaysWithHolidays;
    }
}

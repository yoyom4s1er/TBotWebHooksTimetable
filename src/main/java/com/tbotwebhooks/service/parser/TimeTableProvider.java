package com.tbotwebhooks.service.parser;

import com.tbotwebhooks.util.WeekState;

import java.time.DayOfWeek;

public interface TimeTableProvider {

    public String getTimetableByWeek(String group, WeekState weekState);

    public String getTimetableByDay(String group, DayOfWeek day, WeekState weekState);
}

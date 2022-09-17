package com.tbotwebhooks.util;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.DayOfWeek;

public class DayOfWeekConverter {
    public static DayOfWeek convertToDayOfWeek(String input) {
        DayOfWeek day = null;

        switch (input) {
            case "ПОНЕДЕЛЬНИК" -> day = DayOfWeek.MONDAY;
            case "ВТОРНИК" -> day = DayOfWeek.TUESDAY;
            case "СРЕДА" -> day = DayOfWeek.WEDNESDAY;
            case "ЧЕТВЕРГ" -> day = DayOfWeek.THURSDAY;
            case "ПЯТНИЦА" -> day = DayOfWeek.FRIDAY;
            case "СУББОТА" -> day = DayOfWeek.SATURDAY;
            case "ВОСКРЕСЕНЬЕ" -> day = DayOfWeek.SUNDAY;
            case "1" -> day = DayOfWeek.MONDAY;
            case "2" -> day = DayOfWeek.TUESDAY;
            case "3" -> day = DayOfWeek.WEDNESDAY;
            case "4" -> day = DayOfWeek.THURSDAY;
            case "5" -> day = DayOfWeek.FRIDAY;
            case "6" -> day = DayOfWeek.SATURDAY;
            case "7" -> day = DayOfWeek.SUNDAY;
        }

        return day;
    }
}

package com.tbotwebhooks.service.parser;

import com.tbotwebhooks.util.CurrentWeekState;
import com.tbotwebhooks.util.WeekState;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.WeekFields;
import java.util.*;
import com.tbotwebhooks.util.Empjis;

@Service
@AllArgsConstructor
public class TimeTableProviderImpl implements TimeTableProvider{

    TimeTableParser timeTableParser;

    public String getTimetableByWeek(String group,WeekState weekState) {
        Element table = timeTableParser.getTimeTableByGroup(group, weekState);
        List<Element> elements = table.getElementsByTag("tr");

        Map<String, Map<String, String>> lessons = new LinkedHashMap<>();

        List<String> days = elements.get(0).children().eachText();

        for (int i = 0; i < days.size(); i++) {
            if (days.get(i).contains(" ")) {
                days.set(i, days.get(i).split(" ")[0]);
            }
            lessons.put(days.get(i), new LinkedHashMap<>());
        }

        elements.remove(0);
        int index = 1;
        StringBuilder timetable = new StringBuilder();

        for (Map.Entry<String, Map<String, String>> entry: lessons.entrySet()) {

            timetable.append('*').append(entry.getKey()).append('*').append("\n");

            for (int i = 0; i < elements.size(); i++) {
                String time = elements.get(i).children().first().child(0).text();
                List<String> cellText = elements.get(i).children().get(index).children().eachText();

                if (!cellText.isEmpty()) {
                    timetable.append(Empjis.CLOCK).append(" ").append(time).append("\n");

                    for (int j = 0; j < cellText.size(); j++) {
                        if (cellText.get(j).endsWith(".")) {
                            timetable.append(Empjis.MAN_TEACHER).append(" ").append(cellText.get(j)).append("\n");
                        }
                        else if(cellText.get(j).split(" ")[0].equals("Аудитория")) {
                            timetable.append(Empjis.WORLD_MAP).append(" ").append(cellText.get(j)).append("\n");
                        }
                        else if(cellText.get(j).split(" ")[0].length() == 7 && cellText.get(j).split(" ")[0].charAt(3) == '-') {
                            timetable.append(Empjis.MAN_STUDENT).append(" ").append(cellText.get(j)).append("\n");
                        }
                        else {
                            timetable.append(cellText.get(j)).append("\n");
                        }
                    }

                    timetable.append("\n");
                }
            }

            timetable.append("-------------------").append("\n");

            index++;
        }
        return timetable.toString();
    }

    private Map<String, Map<String, String>> parseTimetableByWeek(String group,WeekState weekState) {
        Element table = timeTableParser.getTimeTableByGroup(group, weekState);
        List<Element> elements = table.getElementsByTag("tr");

        Map<String, Map<String, String>> lessons = new LinkedHashMap<>();

        List<String> days = elements.get(0).children().eachText();

        for (int i = 0; i < days.size(); i++) {
            if (days.get(i).contains(" ")) {
                days.set(i, days.get(i).split(" ")[0]);
            }
            lessons.put(days.get(i), new LinkedHashMap<>());
        }

        elements.remove(0);
        int index = 1;

        for (Map.Entry<String, Map<String, String>> entry: lessons.entrySet()) {


            for (int i = 0; i < elements.size(); i++) {
                String time = elements.get(i).children().first().child(0).text();
                List<String> cellText = elements.get(i).children().get(index).children().eachText();

                StringBuilder splitText = new StringBuilder();

                for (int j = 0; j < cellText.size(); j++) {
                    if (cellText.get(j).endsWith(".")) {
                        splitText.append(Empjis.MAN_TEACHER).append(" ").append(cellText.get(j)).append("\n");
                    }
                    else if(cellText.get(j).split(" ")[0].equals("Аудитория")) {
                        splitText.append(Empjis.WORLD_MAP).append(" ").append(cellText.get(j)).append("\n");
                    }
                    else if(cellText.get(j).split(" ")[0].length() == 7 && cellText.get(j).split(" ")[0].charAt(3) == '-') {
                        splitText.append(Empjis.MAN_STUDENT).append(" ").append(cellText.get(j)).append("\n");
                    }
                    else {
                        splitText.append(cellText.get(j)).append("\n");
                    }
                }

                entry.getValue().put(time, splitText.toString());
            }
            index++;
        }
        return lessons;
    }

    @Override
    public String getTimetableByDay(String group, DayOfWeek day, WeekState weekState) {
        Map<String, Map<String, String>> lessons = parseTimetableByWeek(group, weekState);

        Map<String, String> lesson = new LinkedHashMap<>();
        StringBuilder builder = new StringBuilder();
        String dayWithState = "";

        switch (day){
            case MONDAY -> {lesson = lessons.get("Понедельник"); dayWithState = "*Понедельник";}
            case TUESDAY -> {lesson = lessons.get("Вторник"); dayWithState = "*Вторник";}
            case WEDNESDAY -> {lesson = lessons.get("Среда"); dayWithState = "*Среда";}
            case THURSDAY -> {lesson = lessons.get("Четверг"); dayWithState = "*Четверг";}
            case FRIDAY -> {lesson = lessons.get("Пятница"); dayWithState = "*Пятница";}
            case SATURDAY -> {lesson = lessons.get("Суббота"); dayWithState = "*Суббота";}
            case SUNDAY -> {lesson = lessons.get("Воскресенье"); dayWithState = "*Воскресенье";}
        }

        if (weekState == WeekState.WEEK_ONE) {
            dayWithState += " (нечётная неделя)";
        }
        else {
            dayWithState += " (чётная неделя)";
        }

        if (LocalDate.now(ZoneId.of("Europe/Moscow")).get(WeekFields.ISO.weekOfYear()) % 2 != 0 && weekState == WeekState.WEEK_ONE) {
            dayWithState += " - текущая*";
        }
        else if (LocalDate.now(ZoneId.of("Europe/Moscow")).get(WeekFields.ISO.weekOfYear()) % 2 == 0 && weekState == WeekState.WEEK_TWO) {
            dayWithState += " - текущая*";
        }
        else {
            dayWithState += "*";
        }

        builder.append(dayWithState).append("\n");

        if (lesson != null) {
            for (Map.Entry<String, String> entry: lesson.entrySet()) {

                if (!entry.getValue().isEmpty()) {
                    builder.append(Empjis.CLOCK).append(" ").append(entry.getKey()).append("\n");
                    builder.append(entry.getValue());
                    builder.append("\n");
                }
            }
        }

        return builder.toString();
    }

}

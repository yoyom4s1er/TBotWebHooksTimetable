package com.tbotwebhooks.botapi.handler.messageHandler;

import com.tbotwebhooks.botapi.BotState;
import com.tbotwebhooks.botapi.InputMessageHandler;
import com.tbotwebhooks.service.ReplyMessageService;
import com.tbotwebhooks.service.UserService;
import com.tbotwebhooks.service.parser.TimeTableProviderImpl;
import com.tbotwebhooks.util.WeekState;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Locale;

@Component
@Slf4j
@AllArgsConstructor
public class ShowTimeTableHandler implements InputMessageHandler {

    private ReplyMessageService messageService;
    private TimeTableProviderImpl timeTableProvider;
    private UserService userService;

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandleName() {
        return BotState.SHOW_TIMETABLE;
    }

    private SendMessage processUsersInput(Message message) {
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();
        String inputMessage = message.getText().toUpperCase(Locale.ROOT);

        int week = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);

        SendMessage replyToUser = null;
        DayOfWeek day = null;

        switch (inputMessage) {
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
            default -> replyToUser = new SendMessage(Long.toString(chatId), "incorrect input. Try again");
        }

        StringBuilder lesson = new StringBuilder();

        if (day != null) {
            lesson.append(timeTableProvider.getTimetableByDay(userService.getUser(userId).get().getStudentGroup(), day ,WeekState.WEEK_ONE));
            lesson.append(timeTableProvider.getTimetableByDay(userService.getUser(userId).get().getStudentGroup(), day ,WeekState.WEEK_TWO));

            replyToUser = messageService.getTimetable(chatId, lesson.toString());
        }

        return replyToUser;
    }
}

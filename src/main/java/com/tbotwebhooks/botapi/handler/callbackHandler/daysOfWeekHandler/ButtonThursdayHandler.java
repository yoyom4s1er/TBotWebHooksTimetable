package com.tbotwebhooks.botapi.handler.callbackHandler.daysOfWeekHandler;

import com.tbotwebhooks.botapi.CallbackQueryHandler;
import com.tbotwebhooks.service.ReplyMessageService;
import com.tbotwebhooks.service.UserService;
import com.tbotwebhooks.service.parser.TimeTableProvider;
import com.tbotwebhooks.util.WeekState;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.time.DayOfWeek;

@Component
@AllArgsConstructor
public class ButtonThursdayHandler implements CallbackQueryHandler {

    private ReplyMessageService messageService;
    private TimeTableProvider timeTableProvider;
    private UserService userService;

    @Override
    public SendMessage handle(CallbackQuery callbackQuery) {
        final long chatId = callbackQuery.getMessage().getChatId();
        final long userId = callbackQuery.getFrom().getId();

        StringBuilder lesson = new StringBuilder();

        lesson.append(timeTableProvider.getTimetableByDay(userService.getUser(userId).get().getStudentGroup(), DayOfWeek.THURSDAY , WeekState.WEEK_ONE));
        lesson.append(timeTableProvider.getTimetableByDay(userService.getUser(userId).get().getStudentGroup(), DayOfWeek.THURSDAY ,WeekState.WEEK_TWO));

        return messageService.getTimetable(chatId, lesson.toString());
    }

    @Override
    public String getHandleName() {
        return "buttonThursday";
    }
}

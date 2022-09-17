package com.tbotwebhooks.botapi.handler.callbackHandler;

import com.tbotwebhooks.botapi.CallbackQueryHandler;
import com.tbotwebhooks.service.ReplyMessageService;
import com.tbotwebhooks.service.UserService;
import com.tbotwebhooks.service.parser.TimeTableProvider;
import com.tbotwebhooks.util.CurrentWeekState;
import com.tbotwebhooks.util.WeekState;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
@AllArgsConstructor
public class ButtonTodayHandler implements CallbackQueryHandler {

    private ReplyMessageService messageService;
    private TimeTableProvider timeTableProvider;
    private UserService userService;

    @Override
    public SendMessage handle(CallbackQuery callbackQuery) {
        final long chatId = callbackQuery.getMessage().getChatId();
        final long userId = callbackQuery.getFrom().getId();

        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
        DayOfWeek dayOfWeek = zonedDateTime.getDayOfWeek();
        WeekState weekState = CurrentWeekState.getWeekState(zonedDateTime);

        return messageService.getTimetable(chatId, timeTableProvider.getTimetableByDay(userService.getUser(userId).get().getStudentGroup(), dayOfWeek, weekState));
    }
    @Override
    public String getHandleName() {
        return "buttonToday";
    }
}

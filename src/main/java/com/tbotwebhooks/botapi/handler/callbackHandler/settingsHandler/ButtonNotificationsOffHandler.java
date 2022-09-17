package com.tbotwebhooks.botapi.handler.callbackHandler.settingsHandler;

import com.tbotwebhooks.botapi.BotState;
import com.tbotwebhooks.botapi.CallbackQueryHandler;
import com.tbotwebhooks.model.User;
import com.tbotwebhooks.service.NotificationService;
import com.tbotwebhooks.service.ReplyMessageService;
import com.tbotwebhooks.service.UserService;
import com.tbotwebhooks.service.parser.TimeTableProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@AllArgsConstructor
public class ButtonNotificationsOffHandler implements CallbackQueryHandler {

    private ReplyMessageService messageService;
    private TimeTableProvider timeTableProvider;
    private UserService userService;
    private NotificationService notificationService;

    @Override
    public SendMessage handle(CallbackQuery callbackQuery) {
        final long chatId = callbackQuery.getMessage().getChatId();
        final long userId = callbackQuery.getFrom().getId();

        User user = userService.getUser(userId).get();
        user.setBotState(BotState.SHOW_TIMETABLE);
        user.setNotificationSubscription(false);
        userService.save(user);

        return messageService.getReplyMessage(chatId, "reply.settings.notifications.off");
    }

    @Override
    public String getHandleName() {
        return "buttonNotificationsOff";
    }
}

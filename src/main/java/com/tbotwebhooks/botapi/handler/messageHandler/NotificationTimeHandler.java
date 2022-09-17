package com.tbotwebhooks.botapi.handler.messageHandler;

import com.tbotwebhooks.botapi.BotState;
import com.tbotwebhooks.botapi.InputMessageHandler;
import com.tbotwebhooks.model.User;
import com.tbotwebhooks.service.MainMenuService;
import com.tbotwebhooks.service.NotificationService;
import com.tbotwebhooks.service.ReplyMessageService;
import com.tbotwebhooks.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@AllArgsConstructor
public class NotificationTimeHandler implements InputMessageHandler {

    private ReplyMessageService messageService;
    private UserService userService;
    private MainMenuService mainMenuService;
    private NotificationService notificationService;

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandleName() {
        return BotState.NOTIFICATION_TIME;
    }

    private SendMessage processUsersInput(Message message) {
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();

        String notificationTime = message.getText();
        String[] substr = notificationTime.split(":");
        int hours = 0;
        int minutes = 0;

        SendMessage incorrectInput = new SendMessage(Long.toString(chatId), "incorrect input");

        if (notificationTime.length() != 4 && notificationTime.length() != 5) {
            return incorrectInput;
        }
        if (substr.length != 2) {
            return incorrectInput;
        }
        for (String str: substr) {
            if (str.contains("[a-zA-Z]+")) {
                return incorrectInput;
            }
        }
        hours = Integer.parseInt(substr[0]);
        minutes = Integer.parseInt(substr[1]);

        User user = userService.getUser(userId).get();
        user.setBotState(BotState.SHOW_TIMETABLE);
        user.setNotificationSubscription(true);
        user.setChatId(chatId);
        userService.update(user);

        notificationService.addNotification(userId, hours, minutes);

        return new SendMessage(Long.toString(chatId), "Успешно");
    }
}

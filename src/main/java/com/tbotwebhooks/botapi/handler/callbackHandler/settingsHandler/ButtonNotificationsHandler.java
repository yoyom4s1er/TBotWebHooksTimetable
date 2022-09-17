package com.tbotwebhooks.botapi.handler.callbackHandler.settingsHandler;

import com.tbotwebhooks.botapi.BotState;
import com.tbotwebhooks.botapi.CallbackQueryHandler;
import com.tbotwebhooks.model.User;
import com.tbotwebhooks.service.ReplyMessageService;
import com.tbotwebhooks.service.UserService;
import com.tbotwebhooks.service.parser.TimeTableProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class ButtonNotificationsHandler implements CallbackQueryHandler {

    private ReplyMessageService messageService;
    private TimeTableProvider timeTableProvider;
    private UserService userService;

    @Override
    public SendMessage handle(CallbackQuery callbackQuery) {
        final long chatId = callbackQuery.getMessage().getChatId();
        final long userId = callbackQuery.getFrom().getId();

        userService.setBotState(userId, BotState.NOTIFICATIONS_SETTINGS);
        User user = userService.getUser(userId).get();

        if (user.getNotificationSubscription() == null) {
            user.setNotificationSubscription(false);
            userService.save(user);
        }

        SendMessage replyMessage = null;

        if (user.getNotificationSubscription()) {
            replyMessage = new SendMessage(Long.toString(chatId), "Уведомления включены");
        }
        else {
            replyMessage = new SendMessage(Long.toString(chatId), "Уведомления отключены");
        }

        replyMessage.setReplyMarkup(getInlineButtons(user.getNotificationSubscription()));

        return replyMessage;
    }

    @Override
    public String getHandleName() {
        return "buttonNotifications";
    }

    private InlineKeyboardMarkup getInlineButtons(Boolean notificationState) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonNotificationsOn = new InlineKeyboardButton();
        InlineKeyboardButton buttonNotificationsOff = new InlineKeyboardButton();


        buttonNotificationsOn.setText("Включить");
        buttonNotificationsOff.setText("Выключить");


        buttonNotificationsOn.setCallbackData("buttonNotificationsOn");
        buttonNotificationsOff.setCallbackData("buttonNotificationsOff");


        List<InlineKeyboardButton> keyboardButtonRow1 = new ArrayList<>();
        keyboardButtonRow1.add(buttonNotificationsOn);

        List<InlineKeyboardButton> keyboardButtonRow2 = new ArrayList<>();
        keyboardButtonRow2.add(buttonNotificationsOff);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        if (notificationState) {
            rowList.add(keyboardButtonRow2);
        }
        else {
            rowList.add(keyboardButtonRow1);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}

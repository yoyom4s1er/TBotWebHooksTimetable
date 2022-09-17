package com.tbotwebhooks.botapi.handler.messageHandler;

import com.tbotwebhooks.botapi.BotState;
import com.tbotwebhooks.botapi.InputMessageHandler;
import com.tbotwebhooks.model.User;
import com.tbotwebhooks.service.ReplyMessageService;
import com.tbotwebhooks.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class SettingsHandler implements InputMessageHandler {

    private ReplyMessageService messageService;
    private UserService userService;

    @Override
    public SendMessage handle(Message message) {
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();

        SendMessage replyToUser = null;

        User user = userService.getUser(userId).get();

        if (user.getStudentGroup() == null) {
            userService.setBotState(userId, BotState.ASK_GROUP);
            return  messageService.getReplyMessage(chatId, "reply.nullPointerGroup");
        }

        replyToUser = messageService.getReplyMessage(chatId, "reply.settings.main");
        replyToUser.setReplyMarkup(getInlineButtons());

        return replyToUser;
    }

    @Override
    public BotState getHandleName() {
        return BotState.SETTINGS;
    }

    private InlineKeyboardMarkup getInlineButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonNotifications = new InlineKeyboardButton();


        buttonNotifications.setText("Уведомления");


        buttonNotifications.setCallbackData("buttonNotifications");


        List<InlineKeyboardButton> keyboardButtonRow1 = new ArrayList<>();
        keyboardButtonRow1.add(buttonNotifications);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        rowList.add(keyboardButtonRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}

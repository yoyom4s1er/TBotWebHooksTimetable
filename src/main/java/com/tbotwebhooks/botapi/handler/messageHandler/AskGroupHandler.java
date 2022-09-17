package com.tbotwebhooks.botapi.handler.messageHandler;

import com.tbotwebhooks.botapi.BotState;
import com.tbotwebhooks.botapi.InputMessageHandler;
import com.tbotwebhooks.model.User;
import com.tbotwebhooks.service.MainMenuService;
import com.tbotwebhooks.service.ReplyMessageService;
import com.tbotwebhooks.service.UserService;
import com.tbotwebhooks.service.parser.TimeTableParser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Locale;

@Component
@Slf4j
@AllArgsConstructor
public class AskGroupHandler implements InputMessageHandler {

    private TimeTableParser timeTableParser;
    private ReplyMessageService messageService;
    private UserService userService;
    private MainMenuService mainMenuService;

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandleName() {
        return BotState.ASK_GROUP;
    }

    private SendMessage processUsersInput(Message message) {
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();

        SendMessage replyToUser = null;

        String messageText = message.getText().toUpperCase(Locale.ROOT);

        if (messageText.length() == 6) {
            messageText = messageText.substring(0, 3) + "-" + messageText.substring(3);
        }

        if (timeTableParser.findGroup(messageText)) {
            User user = userService.getUser(userId).get();
            user.setStudentGroup(messageText);
            userService.save(user);

            replyToUser = messageService.getReplyMessage(chatId, "reply.useMenu");
            userService.setBotState(userId, BotState.SHOW_TIMETABLE);
        }
        else {
            replyToUser = messageService.getReplyMessage(chatId, "reply.incorrectGroup");
        }

        return replyToUser;
    }
}
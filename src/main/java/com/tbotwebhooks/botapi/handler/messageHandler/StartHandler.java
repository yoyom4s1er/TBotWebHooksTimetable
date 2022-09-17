package com.tbotwebhooks.botapi.handler.messageHandler;

import com.tbotwebhooks.botapi.BotState;
import com.tbotwebhooks.botapi.InputMessageHandler;
import com.tbotwebhooks.service.ReplyMessageService;
import com.tbotwebhooks.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Slf4j
@AllArgsConstructor
public class StartHandler implements InputMessageHandler {

    private ReplyMessageService messageService;
    private UserService userService;

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandleName() {
        return BotState.START_STATE;
    }

    private SendMessage processUsersInput(Message message) {
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();

        SendMessage replyToUser = messageService.getReplyMessage(chatId, "reply.askGroup");
        userService.setBotState(userId, BotState.ASK_GROUP);

        return replyToUser;
    }
}

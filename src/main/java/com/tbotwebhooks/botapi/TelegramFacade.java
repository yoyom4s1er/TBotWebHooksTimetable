package com.tbotwebhooks.botapi;

import com.tbotwebhooks.model.User;
import com.tbotwebhooks.service.MainMenuService;
import com.tbotwebhooks.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Component
@Slf4j
@AllArgsConstructor
public class TelegramFacade {

    private CallbackContext callbackContext;

    private BotStateContext botStateContext;

    private UserService userService;

    private MainMenuService mainMenuService;


    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage replyMessage = null;

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User: {}, userId: {}, with data: {}",
                    update.getCallbackQuery().getFrom().getUserName(),
                    callbackQuery.getFrom().getId(),
                    update.getCallbackQuery().getData()
            );

            replyMessage = processCallbackQuery(callbackQuery);
        }

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User: {}, userId: {}, chatId: {}, with text: {}",
                    message.getFrom().getUserName(),
                    message.getFrom().getId(),
                    message.getChatId(),
                    message.getText()
            );
            replyMessage = handleInputMessage(message);
        }


        return replyMessage;
    }

    private SendMessage processCallbackQuery(CallbackQuery callbackQuery) {
        SendMessage replyMessage = callbackContext.processCallbackQuery(callbackQuery);
        long userId = callbackQuery.getFrom().getId();
        User user = userService.getUser(userId).get();
        if (user.getStudentGroup() != null) {
            if (replyMessage.getReplyMarkup() == null)
                replyMessage.setReplyMarkup(mainMenuService.getMainMenu());
        }
        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) {
        String inputMsg = message.getText();
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();
        BotState botState;
        SendMessage replyMessage;

        User user = getOrCreateUser(userId, chatId);

        switch (inputMsg) {
            case ("/start"):
                botState = BotState.START_STATE;
                break;
            case ("Расписание"):
                botState = BotState.ASK_TIMETABLE;
                break;
            case ("Настройки"):
                botState = BotState.SETTINGS;
                break;
            default:
                botState = user.getBotState();
                break;
        }

        replyMessage = botStateContext.processInputMessage(botState, message);

        if (user.getStudentGroup() != null) {
            if (replyMessage.getReplyMarkup() == null)
            replyMessage.setReplyMarkup(mainMenuService.getMainMenu());
        }

        return replyMessage;
    }

    private User getOrCreateUser(long userId, long chatId) {
        Optional<User> user = userService.getUser(userId);
        if (user.isPresent()) {
            return user.get();
        }
        else {
            User createdUser = new User(userId);
            createdUser.setBotState(BotState.START_STATE);
            createdUser.setChatId(chatId);
            userService.save(createdUser);
            return createdUser;
        }
    }
}

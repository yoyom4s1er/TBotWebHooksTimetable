package com.tbotwebhooks.botapi.handler.messageHandler;

import com.tbotwebhooks.botapi.BotState;
import com.tbotwebhooks.botapi.InputMessageHandler;
import com.tbotwebhooks.model.User;
import com.tbotwebhooks.service.MainMenuService;
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
public class AskTimeTableHandler implements InputMessageHandler {

    private ReplyMessageService messageService;
    private UserService userService;
    private MainMenuService mainMenuService;

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandleName() {
        return BotState.ASK_TIMETABLE;
    }

    private SendMessage processUsersInput(Message message) {
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();

        SendMessage replyToUser = null;

        User user = userService.getUser(userId).get();

        if (user.getStudentGroup() == null) {
            userService.setBotState(userId, BotState.ASK_GROUP);
            return  messageService.getReplyMessage(chatId, "reply.nullPointerGroup");
        }

        replyToUser = messageService.getReplyMessage(chatId, "reply.askTimetable");
        userService.setBotState(userId, BotState.SHOW_TIMETABLE);
        replyToUser.setReplyMarkup(getInlineButtons());

        return replyToUser;
    }

    private InlineKeyboardMarkup getInlineButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonToday = new InlineKeyboardButton();
        InlineKeyboardButton buttonNextDay = new InlineKeyboardButton();
        InlineKeyboardButton buttonAfterTwoDays = new InlineKeyboardButton();
        InlineKeyboardButton buttonAfterThreeDays = new InlineKeyboardButton();
        InlineKeyboardButton buttonThisWeek = new InlineKeyboardButton();
        InlineKeyboardButton buttonNextWeek = new InlineKeyboardButton();
        InlineKeyboardButton buttonMonday = new InlineKeyboardButton();
        InlineKeyboardButton buttonTuesday = new InlineKeyboardButton();
        InlineKeyboardButton buttonWednesday = new InlineKeyboardButton();
        InlineKeyboardButton buttonThursday = new InlineKeyboardButton();
        InlineKeyboardButton buttonFriday = new InlineKeyboardButton();
        InlineKeyboardButton buttonSaturday = new InlineKeyboardButton();

        buttonToday.setText("Сегодня");
        buttonNextDay.setText("Завтра");
        buttonAfterTwoDays.setText("Послезавтра");
        buttonAfterThreeDays.setText("Через три дня");
        buttonThisWeek.setText("Текущая неделя");
        buttonNextWeek.setText("Следующая неделя");
        buttonMonday.setText("Понедельник");
        buttonTuesday.setText("Вторник");
        buttonWednesday.setText("Среда");
        buttonThursday.setText("Четверг");
        buttonFriday.setText("Пятница");
        buttonSaturday.setText("Суббота");

        buttonToday.setCallbackData("buttonToday");
        buttonNextDay.setCallbackData("buttonNextDay");
        buttonAfterTwoDays.setCallbackData("buttonAfterTwoDays");
        buttonAfterThreeDays.setCallbackData("buttonAfterThreeDays");
        buttonThisWeek.setCallbackData("buttonThisWeek");
        buttonNextWeek.setCallbackData("buttonNextWeek");
        buttonMonday.setCallbackData("buttonMonday");
        buttonTuesday.setCallbackData("buttonTuesday");
        buttonWednesday.setCallbackData("buttonWednesday");
        buttonThursday.setCallbackData("buttonThursday");
        buttonFriday.setCallbackData("buttonFriday");
        buttonSaturday.setCallbackData("buttonSaturday");

        List<InlineKeyboardButton> keyboardButtonRow1 = new ArrayList<>();
        keyboardButtonRow1.add(buttonToday);
        keyboardButtonRow1.add(buttonNextDay);

        List<InlineKeyboardButton> keyboardButtonRow2 = new ArrayList<>();
        keyboardButtonRow2.add(buttonAfterTwoDays);
        keyboardButtonRow2.add(buttonAfterThreeDays);

        List<InlineKeyboardButton> keyboardButtonRow3 = new ArrayList<>();
        keyboardButtonRow3.add(buttonThisWeek);
        keyboardButtonRow3.add(buttonNextWeek);

        List<InlineKeyboardButton> keyboardButtonRow4 = new ArrayList<>();
        keyboardButtonRow4.add(buttonMonday);
        keyboardButtonRow4.add(buttonTuesday);

        List<InlineKeyboardButton> keyboardButtonRow5 = new ArrayList<>();
        keyboardButtonRow5.add(buttonWednesday);
        keyboardButtonRow5.add(buttonThursday);

        List<InlineKeyboardButton> keyboardButtonRow6 = new ArrayList<>();
        keyboardButtonRow6.add(buttonFriday);
        keyboardButtonRow6.add(buttonSaturday);


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        rowList.add(keyboardButtonRow1);
        rowList.add(keyboardButtonRow2);
        rowList.add(keyboardButtonRow3);
        rowList.add(keyboardButtonRow4);
        rowList.add(keyboardButtonRow5);
        rowList.add(keyboardButtonRow6);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}

package com.tbotwebhooks.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@AllArgsConstructor
@Service
public class ReplyMessageService {

    private LocaleMessageService localeMessageService;

    public SendMessage getReplyMessage(long chatId, String replyMessage) {
        return new SendMessage(Long.toString(chatId), localeMessageService.getMessage(replyMessage));
    }

    public SendMessage getReplyMessage(long chatId, String replyMessage, Object[] args) {
        return new SendMessage(Long.toString(chatId), localeMessageService.getMessage(replyMessage, args));
    }

    public SendMessage getTimetable(long chatId, String timetable) {
        SendMessage sendMessage = new SendMessage(Long.toString(chatId), timetable);
        sendMessage.setParseMode("Markdown");
        return sendMessage;
    }
}

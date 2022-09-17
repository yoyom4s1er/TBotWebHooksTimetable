package com.tbotwebhooks.botapi;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface CallbackQueryHandler {

    SendMessage handle(CallbackQuery callbackQuery);

    String getHandleName();
}

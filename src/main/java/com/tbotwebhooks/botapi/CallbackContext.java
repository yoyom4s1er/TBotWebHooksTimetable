package com.tbotwebhooks.botapi;

import com.tbotwebhooks.botapi.BotState;
import com.tbotwebhooks.botapi.CallbackQueryHandler;
import com.tbotwebhooks.botapi.InputMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CallbackContext {
    private final Map<String, CallbackQueryHandler> callbackHandlers = new HashMap<>();

    @Autowired
    public CallbackContext(List<CallbackQueryHandler> callbackHandlers) {
        callbackHandlers.forEach(handler -> this.callbackHandlers.put(handler.getHandleName(), handler));
    }

    public SendMessage processCallbackQuery(CallbackQuery callbackQuery) {
        CallbackQueryHandler currentCallbackQueryHandler = callbackHandlers.get(callbackQuery.getData());
        return currentCallbackQueryHandler.handle(callbackQuery);
    }
}

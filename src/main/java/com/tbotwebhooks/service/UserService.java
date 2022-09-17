package com.tbotwebhooks.service;

import com.tbotwebhooks.botapi.BotState;
import com.tbotwebhooks.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public void save(User user);

    public void update(User user);

    public Optional<User> getUser(long userId);

    public void setBotState(long userId, BotState botState);

    public List<User> getAllByNotificationSubscription(boolean subscription);
}

package com.tbotwebhooks.service;

import com.tbotwebhooks.botapi.BotState;
import com.tbotwebhooks.model.User;
import com.tbotwebhooks.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService{

    UserRepository userRepository;

    @Override
    public void save(User user) {
        if (user.getBotState() == null) {
            user.setBotState(BotState.START_STATE);
            log.info("Added new User in database with userID: {}", user.getUserId());
        }
        userRepository.save(user);
    }

    public void update(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> getUser(long userId) {
        return userRepository.findByUserId(userId);
    }

    @Override
    public void setBotState(long userId, BotState botState) {
        User user = userRepository.findByUserId(userId).get();

        user.setBotState(botState);

        userRepository.save(user);
    }

    @Override
    public List<User> getAllByNotificationSubscription(boolean subscription) {
        return userRepository.findAllByNotificationSubscription(subscription);
    }
}

package com.tbotwebhooks.service;

import com.tbotwebhooks.model.User;
import com.tbotwebhooks.service.parser.TimeTableProvider;
import com.tbotwebhooks.util.CurrentWeekState;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.json.Json;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class NotificationService {

    private final UserService userService;
    private final TimeTableProvider timeTableProvider;

    private final String botToken;

    public NotificationService(UserService userService, TimeTableProvider timeTableProvider, @Value("${telegrambot.botToken}")String botToken) {
        this.userService = userService;
        this.timeTableProvider = timeTableProvider;
        this.botToken = botToken;
    }

    private void manageNotifications() {
        log.info("Method manageNotifications starts");
        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        List<User> users = userService.getAllByNotificationSubscription(true);
        for (User user: users) {
            if (user.getNotificationTime().isBefore(currentDateTime.plusSeconds(30))) {

                LocalDateTime newDateTime = LocalDateTime.of(
                        currentDateTime.getYear(),
                        currentDateTime.getMonth(),
                        currentDateTime.getDayOfMonth(),
                        user.getNotificationTime().getHour(),
                        user.getNotificationTime().getMinute());

                user.setNotificationTime(newDateTime.plusDays(1));
                userService.update(user);
                DayOfWeek dayOfWeek = ZonedDateTime.now(ZoneId.of("Europe/Moscow")).getDayOfWeek();
                String message = timeTableProvider.getTimetableByDay(user.getStudentGroup(), dayOfWeek, CurrentWeekState.getCurrentWeek());
                pushNotification(user.getChatId(), message);
            }
        }
    }

    public void addNotification(long userId, int hours, int minutes) {

        User user = userService.getUser(userId).get();

        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        LocalDateTime notificationDate = LocalDateTime.of(currentDateTime.getYear(), currentDateTime.getMonth(), currentDateTime.getDayOfMonth(), hours, minutes);
        if (notificationDate.isBefore(currentDateTime)) {
            user.setNotificationTime(notificationDate.plusDays(1));
        } else {
            user.setNotificationTime(notificationDate);
        }
        userService.update(user);
    }

    private void pushNotification(long chatId, String lessons) {

        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";

        log.info("Started push method with chatId: {}", chatId);

        String json = Json.createObjectBuilder()
                .add("chat_id", chatId)
                .add("text", lessons)
                .add("parse_mode", "Markdown")
                .build()
                .toString();

        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.put("Content-Type", List.of("application/json"));
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        log.info("Send notification to chatId: {} with status: {}", chatId, result.getStatusCode());
    }

    @Bean
    public ScheduledExecutorService getNotificationsExecutor() {

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        LocalDateTime neededTime = LocalDateTime.of(
                now.getYear(),
                now.getMonth(),
                now.getDayOfMonth(),
                now.getHour(),
                now.plusMinutes(1).getMinute(),
                0);

        long seconds = ChronoUnit.SECONDS.between(now, neededTime);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            manageNotifications();
        };
        executor.scheduleWithFixedDelay(task, seconds, 60, TimeUnit.SECONDS);
        return executor;
    }
}

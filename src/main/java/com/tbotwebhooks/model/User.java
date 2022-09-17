package com.tbotwebhooks.model;

import com.tbotwebhooks.botapi.BotState;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ValueGenerationType;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long userId;

    private long chatId;

    private BotState botState;

    private String studentGroup;

    private Boolean notificationSubscription;

    private LocalDateTime notificationTime;

    public User(long userId) {
        this.userId = userId;
    }
}

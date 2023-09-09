package org.example.Entity;

import lombok.Data;
import org.example.Base.Buttons;

@Data
public class User {
    String chatId;
    String state = Buttons.NEW;
    boolean blocked;

    public User(String chatId) {
        this.chatId = chatId;
        this.blocked = false;
    }
}

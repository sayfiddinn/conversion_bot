package org.example.Controller;


import org.example.Service.UserService;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


public class UserController {

    private final UserService userService=new UserService();

    public void run(Update update, SendMessage sendMessage, SendDocument sendDocument) {
        if (update.getMessage().hasText()) {
            userService.isText(update,sendMessage,sendDocument);
        }
    }

}

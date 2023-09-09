package org.example.Bot;


import org.example.Controller.MainController;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramBot extends TelegramLongPollingBot {
    private final MainController mainController=new MainController();


    @Override
    public void onUpdateReceived(Update update) {
        mainController.run(update,this);
    }

    @Override
    public String getBotUsername() {
        return "bot_username";
    }

    @Override
    public String getBotToken() {
        return "bot_token";
    }

}

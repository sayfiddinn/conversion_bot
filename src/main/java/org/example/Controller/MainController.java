package org.example.Controller;


import org.example.Base.Database;
import org.example.Base.Menu;
import org.example.Bot.TelegramBot;
import org.example.KeyBoardWork.ReplyMarkUp;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Objects;

public class MainController {
    private final UserController userController = new UserController();
    private final AdminController adminController = new AdminController();
    private final ReplyMarkUp replyMarkUp = new ReplyMarkUp();

    public void run(Update update, TelegramBot bot) {
        Long chatId = update.getMessage().getChatId();
        check(update, bot, chatId);


        SendDocument sendDocument = new SendDocument();
        SendMessage sendMessage = new SendMessage();
        sendDocument.setChatId(chatId);
        sendMessage.setChatId(chatId);

        if (Database.adminChatIDs.contains(chatId)) {
            String text = update.getMessage().getText();
            if (Objects.equals(text, "/start")) {
                sendMessage.setText("Kim bo'lib kirmoqchisiz");
                sendMessage.setReplyMarkup(replyMarkUp.getKeyboard(Menu.ADMIN_M));
                Database.adminMap.put(chatId, null);
            } else {
                String role = Database.adminMap.get(chatId);
                if (role != null) text = role;

                switch (text) {
                    case "USER": {
                        userController.run(update, sendMessage, sendDocument);
                        Database.adminMap.put(chatId, "USER");
                    }
                    break;
                    case "ADMIN": {
                        adminController.run(update, sendMessage, sendDocument);
                        Database.adminMap.put(chatId, "ADMIN");
                    }
                    break;
                    default:
                        sendMessage.setText("Kerakli roleni tanlang!");
                }
            }
        } else {
            userController.run(update, sendMessage, sendDocument);
        }

        try {
            if (sendMessage.getText() != null) bot.execute(sendMessage);
            if (sendDocument.getDocument() != null) bot.execute(sendDocument);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    private void check(Update update, TelegramBot bot, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        for (org.example.Entity.User user : Database.userList) {
            if (user.getChatId().equals(chatId.toString())) {
                if (user.isBlocked()) {
                    sendMessage.setText("<strong>Siz bloklangansiz! \uD83E\uDE93</strong>");
                    sendMessage.setParseMode(ParseMode.HTML);
                    try {
                        bot.execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    return;
                }

            }
        }
        String text = update.getMessage().getText();
        String name = update.getMessage().getFrom().getFirstName();
        if (text.equals("/start")) {
            sendMessage.setChatId(update.getMessage().getChatId());
            sendMessage.setText(" Konvertatsiya botiga xush kelibsiz  *" + name + "*");
            sendMessage.setParseMode(ParseMode.MARKDOWNV2);
            try {
                bot.execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

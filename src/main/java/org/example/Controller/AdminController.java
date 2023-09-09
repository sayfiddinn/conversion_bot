package org.example.Controller;


import org.example.Base.Buttons;
import org.example.Base.Menu;
import org.example.KeyBoardWork.ReplyMarkUp;
import org.example.Service.AdminService;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;



public class AdminController {
    private final ReplyMarkUp replyMarkUp=new ReplyMarkUp();
    private final AdminService adminService=new AdminService();
    public void run(Update update, SendMessage sendMessage, SendDocument sendDocument) {
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.setText("Kerakli <b>menyuni</b> tanlang");
        sendMessage.setParseMode(ParseMode.HTML);
        sendMessage.setReplyMarkup(replyMarkUp.getKeyboard(Menu.ADMIN));
        switch (update.getMessage().getText()) {
            case Buttons.VIEW_OFFERS : adminService.ViewOffers(sendMessage);break;
            case Buttons.SHOW_USERS : adminService.ShowUsers(sendMessage);break;
            case Buttons.BLOCKED_USERS : adminService.BlockedUsers(sendMessage);break;
        }
    }


}

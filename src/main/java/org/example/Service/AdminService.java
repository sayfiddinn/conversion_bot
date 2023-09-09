package org.example.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.Base.Buttons;
import org.example.Entity.Taklif;
import org.example.Entity.Userjon;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Objects;

public class AdminService {

    public void ShowUsers(SendMessage sendMessage) {
        sendMessage.setText(getUser());
        sendMessage.setParseMode(ParseMode.HTML);
    }

    public void BlockedUsers(SendMessage sendMessage) {
        String s = getUser();
        s += "<strong><em>\n\n \uD83D\uDCDD Bloklanishi kerak user ning id sini kiriting</em></strong>";
        sendMessage.setText(s);
        sendMessage.setParseMode(ParseMode.HTML);

    }

    private String getUser() {
        try (BufferedReader reader = new BufferedReader(new FileReader(Buttons.PARENT_PATH + "/userData"))) {
            Gson gson = new Gson();
            TypeToken<List<Userjon>> token = new TypeToken<>() {
            };
            StringBuilder s = new StringBuilder();
            int i = 1;
            for (Userjon userjon : gson.fromJson(reader, token)) {
                s.append("\n\n\uD83D\uDFE2<b>").append(i++).append("_User</b>\n");
                if (!Objects.equals(userjon.getId(), null)) s.append("Id : <b>").append(userjon.getId()).append("</b>");
                if (!Objects.equals(userjon.getFirstName(), null))
                    s.append("\nFirstName : <b> ").append(userjon.getFirstName()).append("</b>");
                if (!Objects.equals(userjon.getLastName(), null))
                    s.append("\nLastName : <b> ").append(userjon.getLastName()).append("</b>");
                s.append("\nisBot : <b> ").append(userjon.isBot()).append("</b>");
                if (!Objects.equals(userjon.getUserName(), null))
                    s.append("\nUserName : <b> ").append(userjon.getUserName()).append("</b>");
                if (!Objects.equals(userjon.getLanguageName(), null))
                    s.append("\nlanguageName : <b> ").append(userjon.getLanguageName()).append("</b>");
                if (!Objects.equals(userjon.getIsPremium(), null))
                    s.append("\nisPremium : <b> ").append(userjon.getIsPremium()).append("</b>");
            }
            return s.toString();

        } catch (Exception e) {
            return "Xatolik yuz berdi ! ";
        }
    }

    public void ViewOffers(SendMessage sendMessage) {
        Gson gson = new Gson();
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(Buttons.PARENT_PATH, "Taklif")))) {
            TypeToken<List<Taklif>> typeToken = new TypeToken<>() {
            };
            StringBuilder s = new StringBuilder();
            int i = 1;
            if (reader.ready()) {
                for (Taklif taklif : gson.fromJson(reader, typeToken)) {
                    s.append("\n\n").append(i++).append("_User\n");
                    if (!Objects.equals(taklif.getUserId(), null))
                        s.append("UserId : <b>").append(taklif.getUserId()).append("</b>");
                    s.append("\n Takliflar : \n");
                    int j = 0;
                    for (String s1 : taklif.getText().split("qpl#n#")) {
                        j++;
                        if (j > 1) s.append("\uD83D\uDD34 <b>").append(s1).append("</b>\n");
                    }
                }
                sendMessage.setText(s.toString());
                sendMessage.setParseMode(ParseMode.HTML);
            } else sendMessage.setText("Hozircha hech qanday takiflar mavjud emas");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

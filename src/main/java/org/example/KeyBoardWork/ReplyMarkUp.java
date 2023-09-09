package org.example.KeyBoardWork;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ReplyMarkUp {
    public ReplyKeyboardMarkup getKeyboard(String[][] s) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> list = new ArrayList<>();

        for (String[] rows : s) {
            KeyboardRow row = new KeyboardRow();
            for (String text : rows) {
                KeyboardButton button = new KeyboardButton();
                if (text != null) button.setText(text);
                row.add(button);
            }
            list.add(row);
        }
        markup.setKeyboard(list);
        markup.setResizeKeyboard(true);
        return markup;
    }
}

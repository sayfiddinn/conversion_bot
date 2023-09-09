package org.example.Base;

public interface Menu {

    String[][] KURS_MENU = {
            {Buttons.USD, Buttons.EUR, Buttons.RUB},
            {Buttons.ALL_COURCE, Buttons.EXIT}
    };
    String[][] MAIN_MENU = {
            {Buttons.SHOW_KURS, Buttons.KONVERTATSIYA},
            {Buttons.ALOQA}
    };
    String[][] KURS_SANA = {
            {Buttons.BUGUN, Buttons.KECHA},
            {Buttons.CHOOSE_DATE, Buttons.EXIT}
    };
    String[][] K = {
            {Buttons.KIRITIDIM}
    };
    String[][] ALOQA = {
            {Buttons.ADMIN, Buttons.TAKLIF},
            {Buttons.EXIT}
    };
    String[][] EX = {
            {Buttons.EXIT}
    };
    String[][] KONVERTATSIYA = {
            {Buttons.USD_UZS, Buttons.UZS_USD},
            {Buttons.EUR_UZS, Buttons.UZS_EUR},
            {Buttons.RUB_UZS, Buttons.UZS_RUB},
            {Buttons.TANLASH, Buttons.EXIT}
    };
    String[][] ADMIN = {
            {Buttons.VIEW_OFFERS, Buttons.SHOW_USERS},
            {Buttons.BLOCKED_USERS}
    };
    String[][] ADMIN_M = {
            {"ADMIN", "USER"},
    };
}

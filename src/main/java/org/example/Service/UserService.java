package org.example.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.Base.Buttons;
import org.example.Base.Database;
import org.example.Base.Menu;
import org.example.Entity.*;
import org.example.KeyBoardWork.ReplyMarkUp;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

public class UserService {
    Sana sana;

    User user;
    Taklif taklif;
    Konver konver;
    private final ReplyMarkUp replyMarkUp = new ReplyMarkUp();
    private static final DecimalFormat decor = new DecimalFormat("0.00");

    public void isText(Update update, SendMessage sendMessage, SendDocument sendDocument) {
        addUser(update);
        getAll(update);
        if (update.getMessage().getText().equals("/start")) user.setState(Buttons.NEW);
        switch (user.getState()) {
            case Buttons.NEW:
                TextService(update, sendMessage);
                switch (update.getMessage().getText()) {
                    case Buttons.SHOW_KURS:
                        courseDate(sendMessage);
                        break;
                    case Buttons.KONVERTATSIYA:
                        Conversation(sendMessage);
                        break;
                    case Buttons.ALOQA:
                        contact(sendMessage);
                        break;
                }
                break;
            case Buttons.SANA_STATE:
                switch (update.getMessage().getText()) {
                    case Buttons.BUGUN:
                        today(sendMessage);
                        break;
                    case Buttons.KECHA:
                        YesterdayCourse(sendMessage);
                        break;
                    case Buttons.CHOOSE_DATE:
                        getDate(sendMessage);
                        break;
                    case Buttons.EXIT: {
                        user.setState(Buttons.NEW);
                        addBase();
                        MainMenu(sendMessage);
                    }
                    break;
                }
                break;
            case Buttons.KURS_STATE:
                switch (update.getMessage().getText()) {
                    case Buttons.USD:
                        sendMessage.setText(getCourseQuantity("USD"));
                        break;
                    case Buttons.EUR:
                        sendMessage.setText(getCourseQuantity("EUR"));
                        break;
                    case Buttons.RUB:
                        sendMessage.setText(getCourseQuantity("RUB"));
                        break;
                    case Buttons.ALL_COURCE: {
                        sendMessage.setText(Buttons.OPEN_FILE);
                        getFile(sendDocument, "b", sendMessage);
                    }
                    break;
                    case Buttons.EXIT:
                        courseDate(sendMessage);
                        break;
                }
                break;
            case Buttons.KECHA_KURS_STATE:
                switch (update.getMessage().getText()) {
                    case Buttons.USD:
                        sendMessage.setText(yesterday("USD"));
                        break;
                    case Buttons.EUR:
                        sendMessage.setText(yesterday("EUR"));
                        break;
                    case Buttons.RUB:
                        sendMessage.setText(yesterday("RUB"));
                        break;
                    case Buttons.ALL_COURCE: {
                        sendMessage.setText(Buttons.OPEN_FILE);
                        getFile(sendDocument, "k", sendMessage);
                    }
                    break;
                    case Buttons.EXIT:
                        courseDate(sendMessage);
                        break;
                }
                break;
            case Buttons.SANA_KIRITSH_STATE:

                if (sana.getDate().isEmpty()) {
                    sana.setDate(update.getMessage().getText());
                } else {
                    if (update.getMessage().getText().equals(Buttons.KIRITIDIM)) {
                        sendMessage.setText("Kerakli *kursni* ni tanlang ");
                        sendMessage.setParseMode(ParseMode.MARKDOWNV2);
                        sendMessage.setReplyMarkup(replyMarkUp.getKeyboard(Menu.KURS_MENU));
                        user.setState(Buttons.KIRITISH_KURS_STATE);
                        addBase();
                    } else {
                        sendMessage.setText("To'g'ri <b>menyu</b>ni tanlang ! ");
                        sendMessage.setParseMode(ParseMode.HTML);
                    }
                }
                break;
            case Buttons.KIRITISH_KURS_STATE:
                switch (update.getMessage().getText()) {
                    case Buttons.USD:
                        sendMessage.setText(getCursQuantitySana("USD"));
                        break;
                    case Buttons.EUR:
                        sendMessage.setText(getCursQuantitySana("EUR"));
                        break;
                    case Buttons.RUB:
                        sendMessage.setText(getCursQuantitySana("RUB"));
                        break;
                    case Buttons.ALL_COURCE: {
                        sendMessage.setText(Buttons.OPEN_FILE);
                        getFile(sendDocument, "s", sendMessage);
                    }
                    break;
                    case Buttons.EXIT: {
                        sana.setDate("");
                        courseDate(sendMessage);
                    }
                    break;
                }
                break;
            case Buttons.ALOQA_STATE:
                switch (update.getMessage().getText()) {
                    case Buttons.ADMIN: {
                        sendMessage.setText("☎ Adminning telefon raqami:\n" +
                                "<b>(99) 000-00-00</b>");
                        sendMessage.setParseMode(ParseMode.HTML);
                    }
                    break;
                    case Buttons.EXIT: {
                        user.setState(Buttons.NEW);
                        addBase();
                        MainMenu(sendMessage);
                    }
                    break;
                    case Buttons.TAKLIF: {
                        sendMessage.setText("Taklif va shikoyatlaringizni bu yerda batafsil yozib qoldiring \uD83D\uDCDD");
                        sendMessage.setReplyMarkup(replyMarkUp.getKeyboard(Menu.EX));
                        user.setState(Buttons.TAKLIF_STATE);
                        addBase();
                    }
                    break;

                }
                break;
            case Buttons.TAKLIF_STATE:
                if (update.getMessage().getText().equals(Buttons.EXIT)) {
                    addOffer();
                    contact(sendMessage);
                } else {
                    String text = update.getMessage().getText();
                    if (text.length() < 5) sendMessage.setText("Xabar matni juda ham qisqa \uD83D\uDE42");
                    else {
                        taklif.setText(taklif.getText() + "qpl#n#" + text);
                        sendMessage.setText("Sizning xabaringiz qabul qilindi \uD83D\uDCE8");
                    }
                }
                break;
            case Buttons.KONVERTATSIYA_STATE:
                switch (update.getMessage().getText()) {
                    case Buttons.USD_UZS:
                        getCCy("USD", "UZS", sendMessage);
                        break;
                    case Buttons.UZS_USD:
                        getCCy("UZS", "USD", sendMessage);
                        break;
                    case Buttons.EUR_UZS:
                        getCCy("EUR", "UZS", sendMessage);
                        break;
                    case Buttons.UZS_EUR:
                        getCCy("UZS", "EUR", sendMessage);
                        break;
                    case Buttons.RUB_UZS:
                        getCCy("RUB", "UZS", sendMessage);
                        break;
                    case Buttons.UZS_RUB:
                        getCCy("UZS", "RUB", sendMessage);
                        break;
                    case Buttons.TANLASH:
                        Tanlash(sendMessage);
                    case Buttons.EXIT: {
                        user.setState(Buttons.NEW);
                        addBase();
                        MainMenu(sendMessage);
                    }
                    break;
                }
                break;
            case Buttons.AMOUNT_STATE:
                if (update.getMessage().getText().equals(Buttons.EXIT)) {
                    Conversation(sendMessage);
                    konver.setFirst_ccy("");
                    konver.setSecond_ccy("");
                } else {
                    double amount = Double.parseDouble(update.getMessage().getText());
                    if (amount <= 0) sendMessage.setText("Pul midori xato kiritildi !");
                    else {
                        sendMessage.setText(getAmount(amount));
                        sendMessage.setParseMode(ParseMode.HTML);

                    }
                }
                break;
            case Buttons.KON_TANLASH_STATE:
                if (update.getMessage().getText().equals(Buttons.EXIT)) {
                    Conversation(sendMessage);
                    konver.setFirst_ccy("");
                    konver.setSecond_ccy("");
                } else {

                    if (konver.getFirst_ccy().isEmpty()) {
                        konver.setFirst_ccy(update.getMessage().getText());
                        sendMessage.setParseMode(ParseMode.HTML);
                        sendMessage.setText("Choose <b>second</b> ccy");
                        sendMessage.setParseMode(ParseMode.HTML);
                    } else if (konver.getSecond_ccy().isEmpty()) {
                        konver.setSecond_ccy(update.getMessage().getText());
                        getCCy(konver.getFirst_ccy(), konver.getSecond_ccy(), sendMessage);
                    }
                }
                break;
        }
    }


    private void Tanlash(SendMessage sendMessage) {
        user.setState(Buttons.KON_TANLASH_STATE);
        addBase();
        String[][] mas = new String[38][2];
        try {
            Gson gson = new Gson();
            URL url = new URL("https://cbu.uz/oz/arkhiv-kursov-valyut/json/all/" + sana.getDate());
            List<Valyuta> list = getUrl(gson, url);
            if (list == null) {
                sendMessage.setText("Nmadir xato ketdi");
                return;
            }
            int k = 0;
            for (int i = 0; i < mas.length; i++) {
                for (int j = 0; j < mas[i].length; j++) {
                    if (i != 37 || j != 1) mas[i][j] = list.get(k++).getCcy();
                }
            }
            mas[37][1] = Buttons.EXIT;
        } catch (IOException ignored) {

        }
        sendMessage.setText("Choose <b>first</b> ccy");
        sendMessage.setParseMode(ParseMode.HTML);
        sendMessage.setReplyMarkup(replyMarkUp.getKeyboard(mas));
    }

    private List<Valyuta> getUrl(Gson gson, URL url) throws IOException {
        try {
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            TypeToken<List<Valyuta>> token = new TypeToken<>() {
            };
            return gson.fromJson(reader, token);
        } catch (Exception e) {
            return null;
        }

    }


    public String getAmount(Double amount) {
        String resultWord = "";
        try {
            Gson gson = new Gson();
            String ccy = konver.getFirst_ccy();
            if (konver.getFirst_ccy().equals("UZS")) ccy = konver.getSecond_ccy();
            URL url = new URL("https://cbu.uz/oz/arkhiv-kursov-valyut/json/" + ccy + "/");
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            TypeToken<List<Valyuta>> token = new TypeToken<>() {
            };
            List<Valyuta> list = gson.fromJson(reader, token);
            Valyuta valyuta = list.get(0);
            if (!konver.getFirst_ccy().equals("UZS") && !konver.getSecond_ccy().equals("UZS")) {
                URL url1 = new URL("https://cbu.uz/oz/arkhiv-kursov-valyut/json/" + konver.getSecond_ccy() + "/");
                URLConnection urlConnection1 = url1.openConnection();
                InputStream inputStream1 = urlConnection1.getInputStream();
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(inputStream1));
                list = gson.fromJson(reader1, token);

            }
            double quantity;
            if (konver.getFirst_ccy().equals("UZS")) {
                quantity = amount / Double.parseDouble(valyuta.getRate());
                resultWord = "<b>" + decor.format(amount) + "</b> O'zbek so'mi ->\n <b>" + decor.format(quantity) + "</b> " + valyuta.getCcyNmUZ();
            } else if (konver.getSecond_ccy().equals("UZS")) {
                quantity = amount * Double.parseDouble(valyuta.getRate());
                resultWord = "<b>" + decor.format(amount) + "</b> " + valyuta.getCcyNmUZ() + " ->\n <b>" + decor.format(quantity) + "</b> O'zbek so'mi";
            } else {
                quantity = amount * Double.parseDouble(valyuta.getRate());
                quantity = quantity / Double.parseDouble(list.get(0).getRate());
                resultWord = "<b>" + decor.format(amount) + "</b> " + valyuta.getCcyNmUZ() + " ->\n <b>" + decor.format(quantity) + "</b> " + list.get(0).getCcyNmUZ();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultWord;
    }

    public void getCCy(String first_ccy, String second_ccy, SendMessage sendMessage) {
        sendMessage.setText("Pul midorini kiritng " + "(" + first_ccy + ")");
        konver.setFirst_ccy(first_ccy);
        konver.setSecond_ccy(second_ccy);
        sendMessage.setReplyMarkup(replyMarkUp.getKeyboard(Menu.EX));
        user.setState(Buttons.AMOUNT_STATE);
        addBase();
    }

    public void Conversation(SendMessage sendMessage) {
        sendMessage.setText("Kerakli <b>konvertatsiya</b> turini tanlang !");
        sendMessage.setParseMode(ParseMode.HTML);
        sendMessage.setReplyMarkup(replyMarkUp.getKeyboard(Menu.KONVERTATSIYA));
        user.setState(Buttons.KONVERTATSIYA_STATE);
        addBase();
    }

    public void getAll(Update update) {
        getUser(update);
        getDate(update);
        getOffer(update);
        getConverse(update);
    }

    private void getConverse(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        for (Konver val : Database.konverList) {
            if (val.getUserId().equals(chatId)) {
                konver = val;
                return;
            }
        }
        konver = new Konver();
        konver.setUserId(chatId);
        Database.konverList.add(konver);
    }

    private void getOffer(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        for (Taklif val : Database.taklifList) {
            if (val.getUserId().equals(chatId)) {
                taklif = val;
                return;
            }
        }
        taklif = new Taklif();
        taklif.setUserId(chatId);
        Database.taklifList.add(taklif);
        addOffer();
    }

    public void addOffer() {
        File file = new File(Buttons.PARENT_PATH, "Taklif");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (PrintWriter writer = new PrintWriter(file)) {
            String s = gson.toJson(Database.taklifList);
            writer.println(s);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void contact(SendMessage sendMessage) {
        sendMessage.setText("Kerakli *menyu*ni tanlang");
        sendMessage.setParseMode(ParseMode.MARKDOWNV2);
        sendMessage.setReplyMarkup(replyMarkUp.getKeyboard(Menu.ALOQA));
        user.setState(Buttons.ALOQA_STATE);
        addBase();
    }

    public String getCursQuantitySana(String ccy) {
        String resultWord = "Hech qanday ma'lumot topilmadi !";
        try {
            Gson gson = new Gson();
            URL url = new URL("https://cbu.uz/oz/arkhiv-kursov-valyut/json/" + ccy + "/" + sana.getDate() + "/");
            List<Valyuta> list = getUrl(gson, url);
            if (list == null) return "Hech qanday ma'umot topilmadi";
            if (CheckDate()) {
                if (ccy.equals("USD"))
                    resultWord = sana.getDate() + " sana bo'yicha 1  " + list.get(0).getCcyNmUZ() + " \n" + list.get(0).getRate() + "  So'm " + "\uD83D\uDE05";
                else if (ccy.equals("EUR"))
                    resultWord = sana.getDate() + " sana bo'yicha 1  " + list.get(0).getCcyNmUZ() + " \n" + list.get(0).getRate() + "  So'm " + "\uD83E\uDD2A";
                else
                    resultWord = sana.getDate() + " sana bo'yicha 1  " + list.get(0).getCcyNmUZ() + " \n" + list.get(0).getRate() + "  So'm " + "\uD83E\uDD14";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultWord;
    }

    private boolean CheckDate() {
        if (sana.getDate().length() == 1) return false;
        for (int i = 0; i < sana.getDate().length(); i++)
            if (Character.isAlphabetic(sana.getDate().charAt(i)))
                return false;
        return true;
    }

    private void getDate(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        for (Sana val : Database.sanaList) {
            if (val.getUserId().equals(chatId)) {
                sana = val;
                return;
            }
        }
        sana = new Sana();
        sana.setUserId(chatId);
        Database.sanaList.add(sana);
        addDate();
    }

    private void addDate() {
        File file = new File(Buttons.PARENT_PATH, "Date");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (PrintWriter writer = new PrintWriter(file)) {
            String s = gson.toJson(Database.sanaList);
            writer.println(s);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void getDate(SendMessage sendMessage) {
        sendMessage.setText("Sanani  <b>yil-oy-kun</b> formatida kiriting\nMisol uchun : <b>2000-01-01</b>");
        sendMessage.setParseMode(ParseMode.HTML);
        sendMessage.setReplyMarkup(replyMarkUp.getKeyboard(Menu.K));
        user.setState(Buttons.SANA_KIRITSH_STATE);
        addBase();
    }


    public String yesterday(String ccy) {
        String resultWord = "";
        try {
            Gson gson = new Gson();
            LocalDate localDate = LocalDate.now();
            localDate = localDate.minusDays(1);
            URL url = new URL("https://cbu.uz/oz/arkhiv-kursov-valyut/json/" + ccy + "/" + localDate + "/");
            List<Valyuta> list = getUrl(gson, url);

            resultWord = getResultWord(ccy, localDate, list);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultWord;

    }

    private String getResultWord(String ccy, LocalDate localDate, List<Valyuta> list) {
        String resultWord;
        if (ccy.equals("USD"))
            resultWord = localDate + " sana bo'yicha 1  " + list.get(0).getCcyNmUZ() + " \n" + list.get(0).getRate() + "  So'm " + "\uD83D\uDE05";
        else if (ccy.equals("EUR"))
            resultWord = localDate + " sana bo'yicha 1  " + list.get(0).getCcyNmUZ() + " \n" + list.get(0).getRate() + "  So'm " + "\uD83E\uDD2A";
        else
            resultWord = localDate + " sana bo'yicha 1  " + list.get(0).getCcyNmUZ() + " \n" + list.get(0).getRate() + "  So'm " + "\uD83E\uDD14";
        return resultWord;
    }

    public void YesterdayCourse(SendMessage sendMessage) {
        user.setState(Buttons.KECHA_KURS_STATE);
        addBase();
        sendMessage.setText("Kerakli *kursni* ni tanlang ");
        sendMessage.setParseMode(ParseMode.MARKDOWNV2);
        sendMessage.setReplyMarkup(replyMarkUp.getKeyboard(Menu.KURS_MENU));

    }


    private void getUser(Update update) {

        String chatId = update.getMessage().getChatId().toString();
        for (User val : Database.userList) {
            if (val.getChatId().equals(chatId)) {
                user = val;
                return;
            }
        }
        user = new User(chatId);
        Database.userList.add(user);
        addBase();
    }

    public void addBase() {
        File file = new File(Buttons.PARENT_PATH, "userState");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (PrintWriter writer = new PrintWriter(file)) {
            String s = gson.toJson(Database.userList);
            writer.println(s);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addUser(Update update) {
        for (User user1 : Database.userList) {
            if (update.getMessage().getChatId().toString().equals(user1.getChatId())) {
                return;
            }
        }
        org.telegram.telegrambots.meta.api.objects.User from = update.getMessage().getFrom();
        Userjon userjon = new Userjon();
        userjon.setBot(from.getIsBot());
        userjon.setFirstName(from.getFirstName());
        userjon.setId(from.getId().toString());
        userjon.setIsPremium(from.getIsPremium());
        userjon.setLanguageName(from.getLanguageCode());
        userjon.setLastName(from.getLastName());
        userjon.setUserName(from.getUserName());
        Database.userBazaList.add(userjon);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter fw = new FileWriter("src/main/resources/userData", false);
             PrintWriter writer = new PrintWriter(fw)) {
            String s = gson.toJson(Database.userBazaList);
            writer.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void TextService(Update update, SendMessage sendMessage) {
        if (update.getMessage().hasText()) {
            MainMenu(sendMessage);
        }

    }

    public void MainMenu(SendMessage sendMessage) {
        sendMessage.setText("Kerakli *menyuni* tanlang");
        sendMessage.setParseMode(ParseMode.MARKDOWNV2);
        sendMessage.setReplyMarkup(replyMarkUp.getKeyboard(Menu.MAIN_MENU));

    }

    public void courseDate(SendMessage sendMessage) {
        user.setState(Buttons.SANA_STATE);
        addBase();
        sendMessage.setText("Kerakli *sanani* tanlang");
        sendMessage.setParseMode(ParseMode.MARKDOWNV2);
        sendMessage.setReplyMarkup(replyMarkUp.getKeyboard(Menu.KURS_SANA));
    }

    public void today(SendMessage sendMessage) {
        user.setState(Buttons.KURS_STATE);
        addBase();
        sendMessage.setText("Kerakli *kursni* ni tanlang ");
        sendMessage.setParseMode(ParseMode.MARKDOWNV2);
        sendMessage.setReplyMarkup(replyMarkUp.getKeyboard(Menu.KURS_MENU));

    }

    public String getCourseQuantity(String ccy) {
        String resultWord = "";
        try {
            Gson gson = new Gson();
            URL url = new URL("https://cbu.uz/oz/arkhiv-kursov-valyut/json/" + ccy + "/");
            List<Valyuta> list = getUrl(gson, url);
            LocalDate localDate = LocalDate.now();

            resultWord = getResultWord(ccy, localDate, list);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultWord;
    }

    public void getFile(SendDocument sendDocument, String s, SendMessage sendMessage) {
        String word = "";
        try {
            Gson gson = new Gson();
            LocalDate localDate = LocalDate.now();
            localDate = localDate.minusDays(1);
            String urlWord = "";
            if (s.equals("b")) {
                urlWord = "https://cbu.uz/oz/arkhiv-kursov-valyut/json/";
                word = LocalDate.now().toString();
            }
            if (s.equals("k")) {
                urlWord = "https://cbu.uz/oz/arkhiv-kursov-valyut/json/all/" + localDate + "/";
                word = localDate.toString();
            }
            if (s.equals("s")) {
                urlWord = "https://cbu.uz/oz/arkhiv-kursov-valyut/json/all/" + sana.getDate() + "/";
                word = sana.getDate();
            }
            URL url = new URL(urlWord);
            List<Valyuta> list = getUrl(gson, url);
            if (list == null) {
                sendMessage.setText("Hech qanday ma'lumot topilmadi");
                return;
            }
            File file = new File(Buttons.PARENT_PATH, "currency.xlsx");
            if (!file.exists()) file.createNewFile();
            try (FileOutputStream out = new FileOutputStream(file);
                 XSSFWorkbook workbook = new XSSFWorkbook()) {

                XSSFSheet sheet = workbook.createSheet("Currency list");

                XSSFRow headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("ccy");
                headerRow.createCell(1).setCellValue("ccy Name");
                headerRow.createCell(2).setCellValue("UZS");
                headerRow.createCell(3).setCellValue("date");

                int rowIndex = 0;


                for (Valyuta valyuta : list) {
                    XSSFRow row = sheet.createRow(++rowIndex);
                    row.createCell(0).setCellValue(valyuta.getCcy());
                    row.createCell(1).setCellValue(valyuta.getCcyNmUZ());
                    row.createCell(2).setCellValue(valyuta.getRate());
                    row.createCell(3).setCellValue(word);
                }

                for (int i = 0; i < 4; i++) {
                    sheet.autoSizeColumn(i);
                }
                workbook.write(out);

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (CheckDate()) {
                sendDocument.setDocument(new InputFile(file));
            } else sendMessage.setText("Hech qanday ma'lumot topilmadi !");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

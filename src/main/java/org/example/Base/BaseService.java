package org.example.Base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.Entity.Sana;
import org.example.Entity.Taklif;
import org.example.Entity.User;
import org.example.Entity.Userjon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public interface BaseService {
    static void fromBase() {
        try {
            checkFile();
        } catch (IOException ignored) {
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (BufferedReader reader = new BufferedReader(new FileReader(
                new File(Buttons.PARENT_PATH, "userState")))) {
            TypeToken<List<User>> typeToken = new TypeToken<>() {};
            if (reader.ready()) Database.userList.addAll(gson.fromJson(reader, typeToken));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(
                new File(Buttons.PARENT_PATH, "userData")))) {
            TypeToken<List<Userjon>> typeToken = new TypeToken<>() {};
            if (reader.ready()) Database.userBazaList.addAll(gson.fromJson(reader, typeToken));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(
                new File(Buttons.PARENT_PATH, "Date")))) {
            TypeToken<List<Sana>> typeToken = new TypeToken<>() {};
            if (reader.ready()) Database.sanaList.addAll(gson.fromJson(reader, typeToken));

        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(
                new File(Buttons.PARENT_PATH, "Taklif")))) {
            TypeToken<List<Taklif>> typeToken = new TypeToken<>() {};
            if (reader.ready()) Database.taklifList.addAll(gson.fromJson(reader, typeToken));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void checkFile() throws IOException {
        File file = new File(Buttons.PARENT_PATH, "userState");
        if (!file.exists()) file.createNewFile();
        file = new File(Buttons.PARENT_PATH, "userData");
        if (!file.exists()) file.createNewFile();
        file = new File(Buttons.PARENT_PATH, "Date");
        if (!file.exists()) file.createNewFile();
        file = new File(Buttons.PARENT_PATH, "Taklif");
        if (!file.exists()) file.createNewFile();
    }

}

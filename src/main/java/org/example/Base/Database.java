package org.example.Base;


import org.example.Entity.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface Database {
    List<User> userList = new ArrayList<>();
    List<Userjon> userBazaList = new ArrayList<>();
    List<Sana> sanaList = new ArrayList<>();
    List<Taklif> taklifList = new ArrayList<>();
    List<Konver> konverList = new ArrayList<>();
    List<Long> adminChatIDs = List.of(856845004L);
    HashMap<Long, String> adminMap = new HashMap<>();
}

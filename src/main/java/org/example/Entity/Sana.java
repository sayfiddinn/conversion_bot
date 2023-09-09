package org.example.Entity;

import lombok.Data;

@Data
public class Sana {
    private String date;
    private String userId;

    public Sana() {
        this.date = "";
    }

}

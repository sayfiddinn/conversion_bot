package org.example.Entity;

import lombok.Data;

@Data
public class Taklif {
    String userId;
    String text;

    public Taklif() {
        this.text = "";
    }

}

package org.example.Entity;

import lombok.Data;

@Data
public class Konver {
    private String userId;
    private String first_ccy;
    private String second_ccy;
    private Double amount;

    public Konver() {
        this.first_ccy = "";
        this.second_ccy = "";
        this.amount = -1.0;
    }
}

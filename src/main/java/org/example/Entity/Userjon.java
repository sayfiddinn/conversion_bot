package org.example.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Userjon {
    private String id;
    private String firstName;
    private boolean isBot;
    private String lastName;
    private String userName;
    private String languageName;
    private Boolean isPremium;
}

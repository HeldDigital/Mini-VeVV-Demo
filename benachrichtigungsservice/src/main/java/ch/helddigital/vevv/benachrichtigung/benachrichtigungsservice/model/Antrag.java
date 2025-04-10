package ch.helddigital.vevv.benachrichtigung.benachrichtigungsservice.model;

import lombok.Data;

@Data
public class Antrag {
    private Long id;
    private String vorname;
    private String nachname;
    private String anliegen;
}

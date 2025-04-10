package ch.helddigital.vevv.archivierung.archivierungsservice.model;

import lombok.Data;

@Data
public class Antrag {
    private Long id;
    private String vorname;
    private String nachname;
    private String anliegen;
}

// antragsservice/src/main/java/ch/helddigital/vevv/antrag/antragsservice/dto/AntragDTO.java
package ch.helddigital.vevv.antrag.antragsservice.dto;

import lombok.Data;

@Data
public class AntragDTO {
    private String vorname;
    private String nachname;
    private String anliegen;
}

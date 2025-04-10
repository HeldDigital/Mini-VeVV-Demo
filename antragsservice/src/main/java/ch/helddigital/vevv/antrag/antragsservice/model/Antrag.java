package ch.helddigital.vevv.antrag.antragsservice.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Antrag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vorname;
    private String nachname;
    private String anliegen;

    private LocalDateTime eingangsdatum = LocalDateTime.now();

    //Lombock erzeugt getter und setter
}

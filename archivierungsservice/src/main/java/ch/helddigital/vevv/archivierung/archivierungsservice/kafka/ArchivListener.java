package ch.helddigital.vevv.archivierung.archivierungsservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import ch.helddigital.vevv.archivierung.archivierungsservice.model.Antrag;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ArchivListener {

    private static final String ARCHIV_ORDNER = "./archiv/";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ArchivListener() {
        try {
            Files.createDirectories(Paths.get(ARCHIV_ORDNER));
        } catch (IOException e) {
            System.err.println("❌ Fehler beim Erstellen des Archiv-Ordners: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "antraege", groupId = "archiv-gruppe")
    public void empfangenerAntrag(String nachricht) {
        System.out.println("🗂️ Antrag empfangen zum Archivieren…");
        System.out.println(nachricht);

        String filename = ARCHIV_ORDNER + "antrag-" + UUID.randomUUID() + ".pdf";
        try (FileWriter writer = new FileWriter(filename)) {
            Antrag antrag = objectMapper.readValue(nachricht, Antrag.class);
            String content = "Antrag-ID: " + antrag.getId() + "\n" +
                             "Von: " + antrag.getVorname() + " " + antrag.getNachname() + "\n" +
                             "Anliegen: " + antrag.getAnliegen();
            writer.write(content);
            System.out.println("✅ Antrag archiviert unter: " + filename);
        } catch (IOException e) {
            System.err.println("❌ Fehler beim Schreiben der Datei: " + e.getMessage());
        }
    }
}

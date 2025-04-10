package ch.helddigital.vevv.benachrichtigung.benachrichtigungsservice.kafka;

import ch.helddigital.vevv.benachrichtigung.benachrichtigungsservice.model.Antrag;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AntragListener {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "antraege", groupId = "benachrichtigung-gruppe")
    public void empfangeNachricht(String nachricht) {
        try {
            Antrag antrag = objectMapper.readValue(nachricht, Antrag.class);
            System.out.printf("📩 Neuer Antrag von %s %s: %s%n",
                    antrag.getVorname(),
                    antrag.getNachname(),
                    antrag.getAnliegen()
            );
        } catch (Exception e) {
            System.err.println("❌ Fehler beim Parsen: " + e.getMessage());
        }
    }
}

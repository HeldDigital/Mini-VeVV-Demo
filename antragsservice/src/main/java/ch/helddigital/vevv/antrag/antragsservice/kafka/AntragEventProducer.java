package ch.helddigital.vevv.antrag.antragsservice.kafka;

import ch.helddigital.vevv.antrag.antragsservice.model.Antrag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AntragEventProducer {

    private static final String TOPIC = "antraege";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    // später ersetzen wir das durch echte JSON-Serialisierung (z. B. mit Jackson), aber fürs Demo reicht das erstmal.
    public void sendeAntragEvent(Antrag antrag) {
        String payload = String.format(
            "{\"id\":%d,\"vorname\":\"%s\",\"nachname\":\"%s\",\"anliegen\":\"%s\"}",
            antrag.getId(), antrag.getVorname(), antrag.getNachname(), antrag.getAnliegen()
        );
        kafkaTemplate.send(TOPIC, payload);
    }
}

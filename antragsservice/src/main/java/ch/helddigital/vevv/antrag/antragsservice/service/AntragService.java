// antragsservice/src/main/java/ch/helddigital/vevv/antrag/antragsservice/service/AntragService.java
package ch.helddigital.vevv.antrag.antragsservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ch.helddigital.vevv.antrag.antragsservice.model.Antrag;
import ch.helddigital.vevv.antrag.antragsservice.model.OutboxEvent;
import ch.helddigital.vevv.antrag.antragsservice.dto.AntragDTO;
import ch.helddigital.vevv.antrag.antragsservice.mapper.AntragMapper;
import ch.helddigital.vevv.antrag.antragsservice.repository.AntragRepository;
import ch.helddigital.vevv.antrag.antragsservice.repository.OutboxEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AntragService {

    private final AntragRepository antragRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final AntragMapper antragMapper;
    private final ObjectMapper objectMapper;

    public AntragService(AntragRepository antragRepository,
                         OutboxEventRepository outboxEventRepository,
                         AntragMapper antragMapper,
                         ObjectMapper objectMapper) {
        this.antragRepository = antragRepository;
        this.outboxEventRepository = outboxEventRepository;
        this.antragMapper = antragMapper;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void saveAntragAndQueueKafkaEvent(AntragDTO antragDTO) throws Exception {
        Antrag antrag = antragMapper.toEntity(antragDTO);
        antragRepository.save(antrag);

        OutboxEvent event = new OutboxEvent();
        event.setTopic("antraege");
        event.setKey(antrag.getId().toString());
        event.setPayload(objectMapper.writeValueAsString(antragDTO));

        outboxEventRepository.save(event);
    }
}

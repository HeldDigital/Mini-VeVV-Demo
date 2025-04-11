// antragsservice/src/main/java/ch/helddigital/vevv/antrag/antragsservice/mapper/AntragMapper.java
package ch.helddigital.vevv.antrag.antragsservice.mapper;

import ch.helddigital.vevv.antrag.antragsservice.model.Antrag;
import ch.helddigital.vevv.antrag.antragsservice.dto.AntragDTO;
import org.springframework.stereotype.Component;

@Component
public class AntragMapper {

    public Antrag toEntity(AntragDTO dto) {
        Antrag entity = new Antrag();
        entity.setVorname(dto.getVorname());
        entity.setNachname(dto.getNachname());
        entity.setAnliegen(dto.getAnliegen());
        return entity;
    }

    public AntragDTO toDTO(Antrag entity) {
        AntragDTO dto = new AntragDTO();
        dto.setVorname(entity.getVorname());
        dto.setNachname(entity.getNachname());
        dto.setAnliegen(entity.getAnliegen());
        return dto;
    }
}

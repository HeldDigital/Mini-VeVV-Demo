package ch.helddigital.vevv.antrag.antragsservice.controller;

import ch.helddigital.vevv.antrag.antragsservice.model.Antrag;
import ch.helddigital.vevv.antrag.antragsservice.repository.AntragRepository;
import ch.helddigital.vevv.antrag.antragsservice.kafka.AntragEventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/antrag")
@CrossOrigin(origins = "*")  // Erlaubt Frontend später Zugriff
public class AntragController {

    @Autowired
    private AntragRepository repository;

    @Autowired
    private AntragEventProducer producer;

    @GetMapping
    public List<Antrag> alleAntraege() {
        return repository.findAll();
    }


    @PostMapping
    public Antrag neuerAntrag(@RequestBody Antrag antrag) {
        Antrag gespeichert = repository.save(antrag);
        producer.sendeAntragEvent(gespeichert);
        return gespeichert;
    }
}

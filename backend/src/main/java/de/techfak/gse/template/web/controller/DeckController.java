package de.techfak.gse.template.web.controller;

import de.techfak.gse.template.domain.DeckServiceImpl;
import de.techfak.gse.template.domain.Stapel;
import de.techfak.gse.template.web.Exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/api")
public class DeckController {

    private DeckServiceImpl deckService;

    @Autowired
    public DeckController(DeckServiceImpl deckService) {
        this.deckService = deckService;
    }

    @GetMapping("/decks")
    public List<Stapel> getDecks() {
        return deckService.getDecks();
    }

    @GetMapping("/decks/{id:\\d+}")
    public Stapel getDeckById(@PathVariable("id") final long id) {
        return deckService.getDeckById(id).orElseThrow(BadRequestException::new);
    }
}

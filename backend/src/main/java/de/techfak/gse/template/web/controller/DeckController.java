package de.techfak.gse.template.web.controller;

import de.techfak.gse.template.domain.Date;
import de.techfak.gse.template.domain.Stapel;
import de.techfak.gse.template.domain.Usr;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DeckController {
    @GetMapping("/decks")
    public List<Stapel> getDecks() {
        Date today = new Date(2025, 4,29);
        final List <Stapel> decks = new ArrayList<>();
        decks.add(new Stapel((long) 1, 1, today, true));
        decks.add(new Stapel((long) 2, 1, today, false));
        decks.add(new Stapel((long) 3, 2, today, true));

        return decks;
    }

}

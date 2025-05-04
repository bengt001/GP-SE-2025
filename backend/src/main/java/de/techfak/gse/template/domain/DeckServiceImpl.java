package de.techfak.gse.template.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service //<1>
public class DeckServiceImpl implements DeckService {
    private final DeckRepository deckRepository;

    @Autowired
    public DeckServiceImpl(DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    @Override
    public List<Deck> getDecks() {
        final List<Deck> decks = new ArrayList<>();

        deckRepository.findAll().forEach(decks::add);  //<2>

        return decks;
    }
    @Override
    public Optional<Deck> getDeck(final String id) {
        final Long deckId = Long.valueOf(id); //<1>

        return deckRepository.findById(deckId); //<2>
    }
    @Override
    public Deck addDeck(final Boolean visibility, final List<String> field_of_law) { //<1>
        final Deck deck = new Deck(visibility, field_of_law);
        return deckRepository.save(deck);
    }

    @Override
    public Deck updateDeck(final String id, final Boolean visibility, final List<String> field_of_law) { //<2>
        final Long deckId = Long.valueOf(id);
        final Deck deck = deckRepository.findById(deckId)
                .orElseThrow(); // hier muss eine Fehlermeldung rein

        deck.setVisibility(visibility);
        deck.setField_of_law(field_of_law);

        return deckRepository.save(deck);
    }

}

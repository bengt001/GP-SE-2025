package de.techfak.gse.template.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DeckServiceImpl implements DeckService {
    private final DeckRepository deckRepository;

    @Autowired
    public DeckServiceImpl(DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    @Override
    public List<Deck> getDecks() {
        final List<Deck> decks = new ArrayList<>();

        deckRepository.findAll().forEach(decks::add);

        return decks;
    }
    @Override
    public Optional<Deck> getDeck(final String id) {
        final Long deckId = Long.valueOf(id);

        return deckRepository.findById(deckId);
    }
    @Override
    public Deck addDeck(final Boolean visibility, final List<String> fieldOfLaw) {
        final Deck deck = new Deck(visibility, fieldOfLaw);
        return deckRepository.save(deck);
    }

    @Override
    public Deck updateDeck(final String id, final Boolean visibility, final List<String> fieldOfLaw) {
        final Long deckId = Long.valueOf(id);
        final Deck deck = deckRepository.findById(deckId)
                .orElseThrow();
        // hier muss eine Fehlermeldung rein
        deck.setVisibility(visibility);
        deck.setFieldOfLaw(fieldOfLaw);

        return deckRepository.save(deck);
    }

}

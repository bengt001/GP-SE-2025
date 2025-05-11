package de.techfak.gse.template.domain;

import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

@Configuration
public class DeckServiceImpl implements DeckService {


    public DeckServiceImpl() {
    }

    @Override
    public List<Deck> getAllDecks() {
        return List.of(new Deck((long) 1, 1, LocalDate.now(), true),
                new Deck((long) 2, 2, LocalDate.now(), false),
                new Deck((long) 3, 2, LocalDate.now(), true));
    }

    @Override
    public Optional<Deck> getDeckById(long id) {
        if(id == 1) {
            return Optional.of(new Deck((long) 1, 1, LocalDate.now(), true));
        }
        return Optional.empty();
    }

    @Override
    public List<Deck> getUserDecks(Usr usr) {
        return List.of(new Deck((long) 1, 1, LocalDate.now(), true),
                new Deck((long) 2, 2, LocalDate.now(), false),
                new Deck((long) 3, 2, LocalDate.now(), true));
    }

    @Override
    public Optional<Deck> getUserDeckById(Usr usr, long id) {
        return Optional.empty();
    }

    @Override
    public List<Card> getCards(long deckId) {
        return List.of();
    }

    @Override
    public Optional<Card> getCardById(long deckId, long id) {
        return Optional.empty();
    }

    @Override
    public List<Card> getUserCards(Usr usr, long deckId) {
        return List.of();
    }

    @Override
    public Optional<Card> getUseCardById(Usr usr, long deckId, long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Deck> getNewUserDeck(Usr usr, long deckId) {
        return Optional.empty();
    }
}

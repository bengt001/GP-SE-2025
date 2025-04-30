package de.techfak.gse.template.domain;

import java.util.List;
import java.util.Optional;

public interface DeckService {

    List<Stapel> getDecks();

    Optional<Stapel> getDeckById(long id);

}

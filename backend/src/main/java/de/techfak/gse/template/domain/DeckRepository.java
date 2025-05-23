package de.techfak.gse.template.domain;

import org.springframework.data.repository.CrudRepository;

/**
 * Repository for Decks
 */
public interface DeckRepository extends CrudRepository<Deck, Long> {
}

package de.techfak.gse.template.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

/**
 * Repository for cards.
 */
public interface CardRepository extends CrudRepository<Card, Long> {

    @Query("SELECT c FROM Card c JOIN c.deck d WHERE d.deckId = :deckId AND c.cardId = :cardId")
    Optional<Card> findCardByIdAndDeckId(@Param("cardId") long cardId, @Param("deckId") long deckId);

}

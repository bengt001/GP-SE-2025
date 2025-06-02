package de.techfak.gse.template.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repository for Decks.
 */
public interface DeckRepository extends CrudRepository<Deck, Long> {

    @Query("SELECT d FROM Deck d JOIN d.users u WHERE u.userId = :userId AND d.deckId = :deckId")
    Optional<Deck> findDeckByIdAndUserId(@Param("deckId") long deckId, @Param("userId") String userId);

}

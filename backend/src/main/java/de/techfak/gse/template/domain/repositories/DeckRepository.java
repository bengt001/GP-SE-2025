package de.techfak.gse.template.domain.repositories;

import de.techfak.gse.template.domain.entities.Deck;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Decks.
 */
public interface DeckRepository extends CrudRepository<Deck, Long> {

    @Query("SELECT d FROM Deck d JOIN d.users u WHERE u.userId = :userId AND d.deckId = :deckId")
    Optional<Deck> findDeckByIdAndUserId(@Param("deckId") long deckId, @Param("userId") String userId);

    @Query("SELECT d FROM Deck d JOIN d.users u WHERE u.userId = :userId")
    List<Deck> findDecksByUserId(@Param("userId") String userId);

}

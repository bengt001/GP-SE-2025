package de.techfak.gse.template.domain.repositories;

import de.techfak.gse.template.domain.entities.Deck;
import de.techfak.gse.template.domain.entities.DeckInfo;
import de.techfak.gse.template.domain.entities.Usr;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * The interface Deck info repository.
 */
public interface DeckInfoRepository extends CrudRepository<DeckInfo, Long> {
    @Query("SELECT di.visibility FROM DeckInfo di WHERE di.deckId = :deck AND di.userId = :user")
    Optional<Boolean> findVisibilityByDeckAndUser(@Param("deck") Deck deck, @Param("user") Usr user);


    /**
     * Finds the DeckInfo entity by deck and user.
     *
     * @param deck the Deck entity referenced in DeckInfo.
     * @param user the Usr entity referenced in DeckInfo.
     * @return An Optional containing the DeckInfo with the given deck and user.
     */
    @Query("SELECT di FROM DeckInfo di WHERE di.deckId = :deck AND di.userId = :user")
    Optional<DeckInfo> findByDeckAndUser(@Param("deck") Deck deck, @Param("user") Usr user);
}

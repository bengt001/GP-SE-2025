package de.techfak.gse.template.domain.repositories;

import de.techfak.gse.template.domain.entities.CardInfo;
import de.techfak.gse.template.domain.entities.Deck;
import de.techfak.gse.template.domain.entities.Usr;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * The interface Card info repository.
 */
public interface CardInfoRepository extends CrudRepository<CardInfo, Long> {

    /**
     * This Method returns the cardInfo that have the cardId and UserId given.
     *
     * @param cardId cardId of the card referenced in cardInfo.
     * @param userId userId of the user referenced in cardInfo.
     * @return A CardInfo with the userId and cardId.
     */
    @Query("SELECT ci FROM CardInfo ci WHERE ci.cardId.cardId = :cardId AND ci.userId.userId = :userId")
    Optional<CardInfo> findCardInfoByCardIdAndUserId(@Param("cardId") long cardId, @Param("userId") String userId);

    /**
     * Returns the CardInfo that has the given deck and user.
     *
     * @param deckId the Deck entity referenced in CardInfo.
     * @param userId the Usr entity referenced in CardInfo.
     * @return An Optional containing the CardInfo with the given deck and user.
     */
    @Query("SELECT ci FROM CardInfo ci WHERE ci.deckId = :deck AND ci.userId = :user")
    List<CardInfo> findCardInfoByDeckAndUser(@Param("deck") Deck deck, @Param("user") Usr user);

    /**
     * Returns the CardInfo that has the given deckId, cardId, and userId.
     *
     * @param deckId the Deck entity referenced in CardInfo.
     * @param cardId the Card entity referenced in CardInfo.
     * @param userId the Usr entity referenced in CardInfo.
     * @return An Optional containing the CardInfo with the given deckId, cardId, and userId.
     */
    @Query("SELECT ci FROM CardInfo ci WHERE ci.deckId.deckId = :deckId AND ci.cardId.cardId = :cardId AND ci.userId.userId = :userId")
    Optional<CardInfo> findCardInfoByDeckIdAndCardIdAndUserId(
            @Param("deckId") long deckId,
            @Param("cardId") long cardId,
            @Param("userId") String userId
    );


    //Long countByDeckIdAndRatingEqualsAndUserIdEquals(Long deckId, Rating rating, String userId);
}

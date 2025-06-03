package de.techfak.gse.template.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * The interface Card info repository.
 */
public interface CardInfoRepository extends CrudRepository<CardInfo, Long> {

    /**
     * This Method returns the cardInfo that have the cardId and UserId given.
     * @param cardId cardId of the card referenced in cardInfo.
     * @param userId userId of the user referenced in cardInfo.
     * @return A CardInfo with the userId and cardId.
     */
    @Query("SELECT ci FROM CardInfo ci WHERE ci.cardId = :cardId AND ci.userId = :userId")
    Optional<CardInfo> findCardInfoByCardIdAndUserId(@Param("cardId") long cardId, @Param("userId") String userId);


    //Long countByDeckIdAndRatingEqualsAndUserIdEquals(Long deckId, Rating rating, String userId);
}

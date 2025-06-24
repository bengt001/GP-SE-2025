package de.techfak.gse.template.domain.implementation;

import de.techfak.gse.template.domain.*;
import de.techfak.gse.template.domain.entities.Card;
import de.techfak.gse.template.domain.entities.CardInfo;
import de.techfak.gse.template.domain.entities.Deck;
import de.techfak.gse.template.domain.entities.Usr;
import de.techfak.gse.template.domain.repositories.CardInfoRepository;
import de.techfak.gse.template.domain.repositories.CardRepository;
import de.techfak.gse.template.domain.repositories.DeckRepository;
import de.techfak.gse.template.domain.service.DeckService;
import de.techfak.gse.template.web.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

/**
 * Implementation for the deck service.
 */
@Service
public class DeckServiceImpl implements DeckService {
    private final DeckRepository deckRepository;
    private final CardRepository cardRepository;
    private final CardInfoRepository cardInfoRepository;
    private final SpacedRepetitionAlgorithm sra;

    @Autowired
    public DeckServiceImpl(DeckRepository deckRepository, CardRepository cardRepository,
                          CardInfoRepository cardInfoRepository) {
        this.deckRepository = deckRepository;
        this.cardRepository = cardRepository;
        this.cardInfoRepository = cardInfoRepository;
        this.sra = new SMTwoAnki();
    }

    @Override
    public List<Deck> getAllDecks() {
        final List<Deck> decks = new ArrayList<>();

        deckRepository.findAll().forEach(decks::add);

        return decks;
    }

    @Override
    public Optional<Deck> getDeckById(long id) {
        return deckRepository.findById(id);
    }

    @Override
    public List<Deck> getUserDecks(Usr usr) {
        return usr.getDecks();
    }

    @Override
    public Optional<Deck> getUserDeckById(Usr usr, long id) {
        return deckRepository.findDeckByIdAndUserId(id, usr.getUserId());
    }

    @SuppressWarnings("checkstyle:TrailingComment")
    @Override
    public List<Card> getCards(long deckId) { //KIRILL: muss hier ein optional davor?
        return deckRepository.findById(deckId).map(Deck::getCards)
                .orElse(Collections.emptyList());
    }

    @Override
    public Optional<Card> getCardByIdFromDeck(long deckId, long cardId) {
        return cardRepository.findCardByIdAndDeckId(cardId, deckId);
    }

    @Override
    public List<Card> getUserCards(Usr usr, long deckId) {
        return getCards(deckId);
    }


    //KIRILL: I would never let the user touch the cards.
    //KIRILL: If this is meant to update card Rating this can be implemented
    @Override
    public Optional<Card> updateCard(Usr usr, long deckId, long cardId, Card updatedCard) {
        Optional<Card> tempCard = cardRepository.findCardByIdAndDeckId(cardId, deckId);
        return tempCard.map(card -> {
            card.setCardType(updatedCard.getCardType());
            card.setContent(updatedCard.getContent());
            return cardRepository.save(card);
        });
    }

    //KIRILL: users dont have unique cards?
    //KIRILL: I will assume that i will give you the cardInfo
    @Override
    public Optional<Card> getUseCardById(Usr usr, long deckId, long cardId) {
        String userID = usr.getUserId();
        return cardRepository.findCardByIdAndDeckId(cardId, deckId);
    }

    //KIRILL: I  don't even know what this is supposed to do.
    //KIRILL: Why would a user create a deck
    @Override
    public Optional<Deck> getNewUserDeck(Usr usr, long templateDeckId) {
        //System.out.println("AARGSJNDFNKSFKSDLFBSKJDBNFLSDFSDFÖPISDFSDFSFSDFSKDFNSDFÖSDFOSDFNJSNDFJSD");
        Optional<Deck> tempDeck = getDeck(templateDeckId);
        if (tempDeck.isPresent()) {
            System.out.println("AARGSJNDFNKSFKSDLFBSKJDBNFLSDFSDFÖPISDFSDFSFSDFSKDFNSDFÖSDFOSDFNJSNDFJSD");
            tempDeck.get().getUsers().add(usr);
            deckRepository.save(tempDeck.get());
            //userRepository.save(usr);//this needs to be saved in deck as it is the owner
            return tempDeck;
        }
        return tempDeck;
    }


    @Override
    public Optional<Deck> updateDeck(Usr usr, long deckId, Deck updatedDeck) {
        Optional<Deck> tempDeck = deckRepository.findById(deckId);
        return tempDeck.map(deck -> {
            deck.setVisibility(updatedDeck.getVisibility());
            deck.setFieldOfLaw(updatedDeck.getFieldOfLaw());
            deck.setCards(updatedDeck.getCards());
            return deckRepository.save(deck);
        });
    }

    //##########################################################################
    //Ab hier sind die OG Dennis Funktionen
    @Override
    public Optional<Deck> getDeck(final Long id) {
        return deckRepository.findById(id);
    }

    @Override
    public Deck addDeck(final Boolean visibility, final List<String> fieldOfLaw, int deckId) {
        final Deck deck = new Deck(visibility, fieldOfLaw, deckId);
        return deckRepository.save(deck);
    }

    @Override
    public Deck updateDeck(final Long id, final Boolean visibility, final List<String> fieldOfLaw) {
        final Long deckId = Long.valueOf(id);
        final Deck deck = deckRepository.findById(deckId)
                .orElseThrow(BadRequestException::new);
        // hier muss eine Fehlermeldung rein
        deck.setVisibility(visibility);
        deck.setFieldOfLaw(fieldOfLaw);

        return deckRepository.save(deck);
    }


    @Override
    public Optional<Deck> deleteDeck(long deckId) {
        Optional<Deck> tempDeck = deckRepository.findById(deckId);
        if (tempDeck.isPresent()) {
            deckRepository.delete(tempDeck.get());
            return tempDeck;
        }
        return Optional.empty();
    }

    /**
     * The method rankCard is used to rank a card and update the values in the cardInfo table.
     * @param usr usr used for the id, who has ranked the card.
     * @param deckId deckId not strictly needed but good to have if the database changes.
     * @param cardId cardId for the CardInfo needed.
     * @param rating Rating (Integer from 0 to 3) to rate the card.
     * @return an updated value of the CardInfo.
     */
    @Override
    public Optional<CardInfo> rankCard(Usr usr, long deckId, long cardId, Rating rating) {
        Optional<CardInfo> tempCardInfo = cardInfoRepository.findCardInfoByCardIdAndUserId(cardId, usr.getUserId());
        return tempCardInfo.map(cardInfo -> {
            cardInfo.setRating(rating);
            cardInfo.setSraValues(sra.updateValues(cardInfo.getSraValues(), cardInfo.getRating()));
            cardInfo.setNextRepetition(LocalDate.now().plusDays(cardInfo.getSraValues().interval()));
            return cardInfoRepository.save(cardInfo);
        });
    }

    /**
     * The method returns the number of cards that are rated as easy, good, hard, again and not learned as a list.
     * @ usr used for the id.
     * @ deckId The deck to get.
     * @return an List of five integers.
     */
//    @Override
//    public Dictionary<Rating, Long> getDeckInfo(Usr usr, long deckId) {
//        Dictionary<Rating, Long> ratingCount = new Hashtable<>();
//        for (Rating rating : Rating.values()) {
//            ratingCount.put(rating, cardInfoRepository.countByDeckIdAndRatingEqualsAndUserIdEquals(
//                    deckId, rating, usr.getUserId()));
//        }
//        return ratingCount;
//    }
}

package de.techfak.gse.template.domain.implementation;

import de.techfak.gse.template.domain.*;
import de.techfak.gse.template.domain.entities.*;
import de.techfak.gse.template.domain.repositories.*;
import de.techfak.gse.template.domain.service.DeckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation for the deck service.
 */
@Service
public class DeckServiceImpl implements DeckService {
    private final DeckRepository deckRepository;
    private final CardRepository cardRepository;
    private final CardInfoRepository cardInfoRepository;
    private final DeckInfoRepository deckInfoRepository;
    private final UserRepository userRepository;
    private final SpacedRepetitionAlgorithm sra;

    /**
     * Instantiates a new Deck service.
     *
     * @param deckRepository     the deck repository
     * @param cardRepository     the card repository
     * @param cardInfoRepository the card info repository
     * @param deckInfoRepository the deck info repository
     * @param userRepository     the user repository
     */
    @Autowired
    public DeckServiceImpl(DeckRepository deckRepository, CardRepository cardRepository,
                           CardInfoRepository cardInfoRepository, DeckInfoRepository deckInfoRepository,UserRepository userRepository) {
        this.deckRepository = deckRepository;
        this.cardRepository = cardRepository;
        this.cardInfoRepository = cardInfoRepository;
        this.deckInfoRepository = deckInfoRepository;
        this.userRepository = userRepository;
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
        List<Deck> usrDecks = usr.getDecks();
        for (int i = 0; i < usrDecks.size(); i++) {
            System.out.println("getUserDecks: " + usrDecks.size());
            Deck tempDeck = usrDecks.get(i);
            Optional<Boolean> tempVisi = deckInfoRepository.findVisibilityByDeckAndUser(tempDeck, usr);
            tempVisi.ifPresent(tempDeck::setVisibility);
        }
        return usrDecks;
    }

    @Override
    public Optional<Deck> getUserDeckById(Usr usr, long id) {
        Optional<Deck> tmpDeck = deckRepository.findDeckByIdAndUserId(id, usr.getUserId());
        if (tmpDeck.isPresent()) {
            Optional<Boolean> tempVisi = deckInfoRepository.findVisibilityByDeckAndUser(tmpDeck.get(), usr);
            tempVisi.ifPresent(tmpDeck.get()::setVisibility);
        }
        return tmpDeck;
    }


    @Override
    public List<Card> getCards(long deckId) {
        return deckRepository.findById(deckId).map(Deck::getCards)
                .orElse(Collections.emptyList());
    }

    @Override
    public Optional<Card> getCardByIdFromDeck(long deckId, long cardId) {
        return cardRepository.findCardByIdAndDeckId(cardId, deckId);
    }


    //Kirill:This will only get you cardInfo
    @Override
    public List<CardInfo> getUserCards(Usr usr, long deckId) {
        Optional<Deck> deck = deckRepository.findById(deckId);
        if (deck.isPresent()) {
            return cardInfoRepository.findCardInfoByDeckAndUser(deck.get(), usr);
        }
        return new ArrayList<CardInfo>();
    }


    //KIRILL: THIS WILL ONLY ALLOW EDITING THE CONTENT
    @Override
    public Optional<Card> updateCard(Usr usr, long deckId, long cardId, Card updatedCard) {
        Optional<Card> tempCard = cardRepository.findCardByIdAndDeckId(cardId, deckId);
        if (tempCard.isPresent()) {
            Optional<CardInfo> tempCardInfo = cardInfoRepository.findCardInfoByDeckIdAndCardIdAndUserId(
                    deckId, cardId, usr.getUserId());
            if (tempCardInfo.isPresent()) {
                tempCardInfo.get().setEditedContent(updatedCard.getContent());
                cardInfoRepository.save(tempCardInfo.get());
                tempCard.get().setContent(updatedCard.getContent());
            }
        }
        return tempCard;
    }
    /*        return tempCard.map(card -> {
            card.setCardType(updatedCard.getCardType());
            card.setContent(updatedCard.getContent());
            return cardRepository.save(card);
        });*/


    //Kirill:This will only get you cardInfo
    @Override
    public Optional<CardInfo> getUseCardById(Usr usr, long deckId, long cardId) {
        String userID = usr.getUserId();
        return cardInfoRepository.findCardInfoByCardIdAndUserId(cardId, userID);
    }

    //KIRILL: I  don't even know what this is supposed to do.
    //KIRILL: Why would a user create a deck
    @Override
    public Optional<Deck> getNewUserDeck(Usr usr, long templateDeckId) {
        Optional<Deck> tempDeck = getDeck(templateDeckId);
        if (tempDeck.isPresent()) {
            System.out.println("getNewUserDeck");
            tempDeck.get().getUsers().add(usr);
            usr.getDecks().add(tempDeck.get());
            deckRepository.save(tempDeck.get());
            //I am not checking the existence of DeckInfo
            DeckInfo newDeckInfo = new DeckInfo(usr, tempDeck.get(), true);
            deckInfoRepository.save(newDeckInfo);
            List<Card> cards = getCards(templateDeckId);
            for (int i = 0; i < cards.size(); i++) {
                Card card = cards.get(i);
                long cardId = card.getCardId();
                CardInfo cardInfo = new CardInfo(usr, card, tempDeck.get(), Rating.NOT_LEARNED);
                cardInfoRepository.save(cardInfo);
            }
            //userRepository.save(usr);
            // this needs to be saved in deck as it is the owner
            return tempDeck;
        }
        return tempDeck;
    }

    //KIRILL: ONLY UPDATES VISIBILITY
    @Override
    public Optional<Deck> updateDeck(Usr usr, long deckId, Deck updatedDeck) {
        Optional<Deck> tempDeck = deckRepository.findById(deckId);
        if (tempDeck.isPresent()) {
            Optional<DeckInfo> tempDeckInfo = deckInfoRepository.findByDeckAndUser(tempDeck.get(), usr);
            if (tempDeckInfo.isPresent()) {
                tempDeckInfo.get().setVisibility(updatedDeck.getVisibility());
                tempDeck.get().setVisibility(updatedDeck.getVisibility());
                deckInfoRepository.save(tempDeckInfo.get());
            }

        }
        return tempDeck;
        /*return tempDeck.map(deck -> {
            deck.setFieldOfLaw(updatedDeck.getFieldOfLaw());
            deck.setCards(updatedDeck.getCards());
            return deckRepository.save(deck);
        });*/
    }

    //##########################################################################
    //Ab hier sind die OG Dennis Funktionen
    @Override
    public Optional<Deck> getDeck(final Long id) {
        return deckRepository.findById(id);
    }


    //Kirill: Use getNewUserDeck to add decks to the User.
    @Override
    public Deck addDeck(final Boolean visibility, final List<String> fieldOfLaw, int deckId) {
        final Deck deck = new Deck(visibility, fieldOfLaw, deckId);
        return deckRepository.save(deck);
    }

    /*@Override
    public Deck updateDeck(final Long id, final Boolean visibility, final List<String> fieldOfLaw) {
        final Long deckId = Long.valueOf(id);
        final Deck deck = deckRepository.findById(deckId)
                .orElseThrow(BadRequestException::new);
        // hier muss eine Fehlermeldung rein
        deck.setVisibility(visibility);
        deck.setFieldOfLaw(fieldOfLaw);

        return deckRepository.save(deck);
    }*/


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
     *
     * @param usr    usr used for the id, who has ranked the card.
     * @param deckId deckId not strictly needed but good to have if the database changes.
     * @param cardId cardId for the CardInfo needed.
     * @param rating Rating (Integer from 0 to 3) to rate the card.
     * @return an updated value of the CardInfo.
     */
    @Override
    public Optional<CardInfo> rankCard(Usr usr, long deckId, long cardId, Rating rating) {
        Optional<CardInfo> tempCardInfo = cardInfoRepository.findCardInfoByDeckIdAndCardIdAndUserId(deckId,cardId, usr.getUserId());
        return tempCardInfo.map(cardInfo -> {
            cardInfo.setRating(rating);
            cardInfo.setSraValues(sra.updateValues(cardInfo.getSraValues(), cardInfo.getRating()));
            cardInfo.setNextRepetition(LocalDate.now().plusDays(cardInfo.getSraValues().interval()));
            return cardInfoRepository.save(cardInfo);
        });
    }


    @Override
    public List<CardInfoCardDTO> getMaxLearningCards(Usr usr, long[] deckId, int maxCards, String[] cardTypes) {
        List<CardInfoCardDTO> cardsAndInfo = new ArrayList<>();
        for (int i = 0; i < deckId.length; i++) {
            Optional<Deck> deck = getUserDeckById(usr, deckId[i]);
            if (deck.isPresent()) {
                if ((deck.get().getCards().size() == getUserCards(usr, deckId[i]).size())
                        && !getUserCards(usr, deckId[i]).isEmpty()) {
                    for (int j = 0; j < deck.get().getCards().size(); j++) {
                        if (Arrays.asList(cardTypes).contains(deck.get().getCards().get(j).getCardType())) {
                            cardsAndInfo.add(new CardInfoCardDTO(getUserCards(usr, deckId[i]).get(j),
                                    deck.get().getCards().get(j)));
                        }
                    }
                }
            }
        }
        LocalDate today = LocalDate.now().plusDays(1);
        cardsAndInfo = cardsAndInfo.stream()
                .filter(dto -> dto.getCardInfo().getNextRepetition().isBefore(today))
                .sorted(Comparator.comparing(dto -> dto.getCardInfo().getNextRepetition()))
                .limit(maxCards)
                .collect(Collectors.toList());
        return cardsAndInfo;
    }

    @Override
    public Optional<CardInfo> getCardInfo(long deckId, long cardId, String userId) {
        Optional<CardInfo>  info = cardInfoRepository.findCardInfoByDeckIdAndCardIdAndUserId(deckId,cardId,userId);
        if (info.isPresent()) {
            return info;
        }
        Optional<Deck> deckOpt = deckRepository.findById(deckId);
        Optional<Card> cardOpt = cardRepository.findById(cardId);
        Optional<Usr> userOpt = userRepository.findById(userId);

        if (deckOpt.isEmpty() || cardOpt.isEmpty() || userOpt.isEmpty()) {
            return Optional.empty();
        }

        CardInfo newInfo = new CardInfo(userOpt.get(),cardOpt.get(),deckOpt.get(),Rating.NOT_LEARNED);
        cardInfoRepository.save(newInfo);
        return Optional.of(newInfo);

    }
}

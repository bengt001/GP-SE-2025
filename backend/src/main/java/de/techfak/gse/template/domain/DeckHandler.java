package de.techfak.gse.template.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * The DeckHandler has helper functions for the deck for rating cards and interacting with them.
 */
public class DeckHandler {
    public static final int AGAIN_CASE = 0;
    public static final int HARD_CASE = 1;
    public static final int GOOD_CASE = 2;
    public static final int EASY_CASE = 3;
    private final SpacedRepetitionAlgorithm sra;

    /**
     * Constructor it initializes the SpacedRepetitionAlgorithm.
     */
    public DeckHandler() {
        this.sra = new SMTwoAnki();
    }

    /**
     * Function to rate a card.
     * @param deck The deck the card is from.
     * @param cardId The Card Id, so not to give the whole card.
     * @param rating The new rating of the card.
     * @return A new deck.
     */
    public Deck rateCard(Deck deck, Long cardId, Integer rating) {
        int cardIndex = getCardIndex(deck, cardId);
        List<Card> cardList = deck.getCards();
        cardList.get(cardIndex).setRating(rating);
        cardList.get(cardIndex).setSraValues(sra.updateValues(cardList.get(cardIndex).getSraValues(), rating));
        cardList.get(cardIndex).setNextRevision(
                LocalDate.now().plusDays(cardList.get(cardIndex).getSraValues().interval()));
        return deck;
    }

    /**
     * Returns a list with the rating status of the card.
     * @param deck The deck the cards are in,
     * @return List of integers in following order : #easy, #good, #hard, #again, #notStarted.
     */
    public List<Integer> getCardsStatus(Deck deck) {
        int easy = 0;
        int good = 0;
        int hard = 0;
        int again = 0;
        int notStarted = 0;

        for (Card card : deck.getCards()) {
            switch (card.getRating()) {
                case -1:
                    notStarted++;
                    break;
                case AGAIN_CASE:
                    again++;
                    break;
                case HARD_CASE:
                    hard++;
                    break;
                case GOOD_CASE:
                    good++;
                    break;
                case EASY_CASE:
                    easy++;
                    break;
                default:
                    break;
            }
        }
        return new ArrayList<>(Arrays.asList(easy, good, hard, again, notStarted));
    }

    /**
     * The method allLearned checks if all Cards that are due today are learned.
     * It checks if the NextRevision of every card is before today or is today and also if this card is rated.
     * Unrated cards (-1) are not learned and won't therefore not be included.
     * @param deck The deck the cards are stored in.
     * @return Boolean if all cards due today are learned (True if all cards are learned)
     */
    public boolean allLearnedToday(Deck deck) {
        return allLearnedTodayList(deck.getCards());
    }

    /**
     * The function allRated returns a boolean if all cards of the deck are rated.
     * @param deck The Deck the cards are stored in.
     * @return Boolean if all cards are rated (true if all cards are rated).
     */
    public boolean allRated(Deck deck) {
       return allRatedList(deck.getCards());
    }

    /**
     * The method getNextCard takes a deck and a card type and returns the next to learn card.
     * It takes into account, that cards that are ranked (learned before) and due today or earlier are served first.
     * Then the new cards will be served and as last the ranked cards that are in the future.
     * @param deck Deck from which the cards are from.
     * @param type The card type.
     * @return A card that is to be learned.
     */
    public Card getNextCard(Deck deck, String type) {
        List<Card> cards = getCardsByType(deck, type);
        LocalDate today = LocalDate.now();
        if (cards.isEmpty()) {
            throw new NoCardException("There is no card with type " + type);
        }

        cards.sort(new Comparator<Card>() {

            /**
             * Method that compares two cards.
             * First are cards that are ranked and due today or before.
             * Second are unranked cards.
             * Third are ranked cards that are due in the future
             * @param cardOne the first object to be compared.
             * @param cardTwo the second object to be compared.
             * @return A int comparator of which card is first.
             */
            @Override
            public int compare(Card cardOne, Card cardTwo) {
                int cardOneGroup = getCardGroup(cardOne, today);
                int cardTwoGroup = getCardGroup(cardTwo, today);

                if (cardOneGroup != cardTwoGroup) {
                    return Integer.compare(cardOneGroup, cardTwoGroup);
                }

                int dateCompare = cardOne.getNextRevision().compareTo(cardTwo.getNextRevision());
                if (dateCompare != 0) {
                    return dateCompare;
                }

                return Integer.compare(cardOne.getRating(), cardTwo.getRating());
            }

            /**
             * Determines in which group the card is.
             * Group 0: Cards that were ranked before and are due today or before.
             * Group 1: Cards that were not ranked before aka new cards.
             * Group 2: Cards that were ranked before and are due in the future.
             * @param card Card that should be ranked
             * @param today Todays date.
             * @return returns an Integer from 0 to 2, depending on the group.
             */
            private int getCardGroup(Card card, LocalDate today) {
                if (card.getRating() >= 0 && !card.getNextRevision().isAfter(today)) {
                    return 0;
                } else if (card.getRating() == -1) {
                    return 1;
                } else {
                    return 2;
                }
            }
        });
        return cards.getFirst();
    }

    /**
     * Returns the index of a Card in a deck.
     * @param cardId cardId to be searched for.
     * @return int Index of the list where the card is.
     */
    private int getCardIndex(Deck deck, Long cardId) {
        for (int i = 0; i < deck.getCards().size(); i++) {
            if (deck.getCards().get(i).getCardId().equals(cardId)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Card not found");
    }

    /**
     * The method allLearnedList checks if all Cards that are due today are learned.
     * It checks if the NextRevision of every card is before today or is today and also if this card is rated.
     * Unrated cards (-1) are not learned and won't therefore not be included.
     * @param cardList  The deck the cards are stored in.
     * @return Boolean if all cards due today are learned (True if all cards are learned)
     */
    private boolean allLearnedTodayList(List<Card> cardList) {
        boolean allLearned = true;
        for (Card card : cardList) {
            if (card.getNextRevision().isBefore(LocalDate.now())
                    || card.getNextRevision().isEqual(LocalDate.now())
                    && card.getRating() != -1) {
                allLearned = false;
                break;
            }
        }
        return allLearned;
    }

    /**
     * The function allRatedList returns a boolean if all cards of the deck are rated.
     * @param cardList The Deck the cards are stored in.
     * @return Boolean if all cards are rated (true if all cards are rated).
     */
    private boolean allRatedList(List<Card> cardList) {
        boolean allRated = true;
        for (Card card : cardList) {
            if (card.getRating() == -1) {
                allRated = false;
                break;
            }
        }
        return allRated;
    }

    /**
     * The function getCardsByType returns a list of all cards from a deck with a specific type.
     * @param deck The deck that stores the cards.
     * @param type The type of the cards.
     * @return A list of cards.
     */
    private List<Card> getCardsByType(Deck deck, String type) {
        List<Card> cards = new ArrayList<>();
        for (Card card : deck.getCards()) {
            if (card.getCardType().equals(type)) {
                cards.add(card);
            }
        }
        return cards;
    }
}


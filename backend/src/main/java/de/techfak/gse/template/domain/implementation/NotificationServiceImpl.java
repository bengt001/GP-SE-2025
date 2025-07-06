package de.techfak.gse.template.domain.implementation;

import de.techfak.gse.template.domain.entities.*;
import de.techfak.gse.template.domain.repositories.*;
import de.techfak.gse.template.domain.service.NotificationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service that manages the notification of users.
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    /**
     * Service that manages the notifications of user.
     */
    private final NotificationRepository notificationRepository;
    /**
     * Service that manages all user interactions.
     */
    private final UserRepository userRepository;
    /**
     * Service that manages all deck interactions.
     */
    private final DeckRepository deckRepository;
    /**
     * Service that manages all cardInfo interactions.
     */
    private final CardInfoRepository cardInfoRepository;
    /**
     * Service that manages all dueDeckInfo interactions.
     */
    private final DueDeckInfoRepository dueDeckInfoRepository;
    /**
     * Service that manages all deckInfo interactions.
     */
    private final DeckInfoRepository deckInfoRepository;

    /**
     * Constructor for NotificationService.
     *
     * @param notificationRepository notificationRepository
     * @param userRepository         userRepository
     * @param deckRepository         deckRepository
     * @param cardInfoRepository     cardInfoRepository
     * @param dueDeckInfoRepository  dueDeckInfo Repository
     * @param deckInfoRepository     deckInfoRepository
     */
    @Autowired
    public NotificationServiceImpl(final NotificationRepository notificationRepository, UserRepository userRepository,
                                   DeckRepository deckRepository, CardInfoRepository cardInfoRepository,
                                   DueDeckInfoRepository dueDeckInfoRepository, DeckInfoRepository deckInfoRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.deckRepository = deckRepository;
        this.cardInfoRepository = cardInfoRepository;
        this.dueDeckInfoRepository = dueDeckInfoRepository;
        this.deckInfoRepository = deckInfoRepository;
    }

    @Override
    public AbstractNotification getNotificationById(@PathVariable Long id) {
        return notificationRepository.findById(id).orElse(null);
    }

    @Override
    public List<AbstractNotification> getNotificationByUser(Usr user) {
        final List<AbstractNotification> notifications = new ArrayList<>();
        List<AbstractNotification> temp = new ArrayList<>();
        notificationRepository.findAll().forEach(temp::add);

        for (AbstractNotification notification : temp) {
            if (notification.getUser().equals(user)) {
                notifications.add(notification);
            }
        }

        return notifications;
    }

    /**
     * Sends daily message to user.
     * TODO: Change to midnight oder 6 am after SRA implemantation
     */
    @Scheduled(cron = "0 */1 * * * *")
    @Transactional
    public void sendDueCardNoteToUsers() {
        List<Usr> users = (List<Usr>) userRepository.findAll();
        for (Usr user : users) {
            sendDueCardNote(user);
        }
    }

    private void sendDueCardNote(Usr user) {
        HashMap<Deck, Integer> decks = getNumberOfDueCardsPerDeck(user);

        DueCardsNotification note = new DueCardsNotification(user);
        notificationRepository.save(note);

        List<DueDeckInfo> temp = new ArrayList<>();

        for (Deck deck : decks.keySet()) {
            DueDeckInfo dueDeckInfo = new DueDeckInfo(note, deck,
                    decks.get(deck));
            dueDeckInfoRepository.save(dueDeckInfo);
            temp.add(dueDeckInfo);
        }
        note.setDueDecks(temp);
    }

    public void sendFriendRequestNote(Usr recipient, Usr requester) {
        FriendRequestNotification note = new FriendRequestNotification(recipient, requester);
        notificationRepository.save(note);
    }

    @Transactional
    @Override
    public boolean deleteNotificationById(Long id) {
        Optional<AbstractNotification> note = notificationRepository.findById(id);
        if (note.isPresent()) {
            AbstractNotification notification = note.get();
            notificationRepository.delete(notification);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteNotificationByUser(Usr user) {
        return false;
    }

    @Transactional
    @Override
    public boolean markNotificationAsRead(Long id) {
        Optional<AbstractNotification> note = notificationRepository.findById(id);
        if (note.isPresent()) {
            AbstractNotification notification = note.get();
            notification.setRead(true);
            notificationRepository.save(notification);
            return true;
        }
        return false;
    }

    public void sendWelcomeNote(Usr user) {
        notificationRepository.save(new WelcomeNotification(user));
    }

    /**
     * Returns the DueDeckInfo for one note.
     *
     * @param notification Notification about dueCards
     * @return List of due Decks with their number of due cards at the time of note creation
     */
    @Override
    public List<DueDeckInfo> getDueDeckInfos(AbstractNotification notification) {
        return dueDeckInfoRepository.findAllDueDecksByNote(notification);
    }

    private HashMap<Deck, Integer> getNumberOfDueCardsPerDeck(Usr user) {
        List<String> cardTypeList = Arrays.asList("Probleme", "Definitionen", "Aufdeckkarte");

        List<CardInfoCardDTO> cardsAndInfo = new ArrayList<>();
        List<Deck> userDecks = deckRepository.findDecksByUserId(user.getUserId());

        for (Deck deck : userDecks) {
            List<CardInfo> userCards = getUserCards(user, deck.getDeckId());
            if ((deck.getCards().size() == userCards.size() && !userCards.isEmpty())) {
                for (int i = 0; i < userCards.size(); i++) {
                    CardInfo cardInfo = userCards.get(i);
                    if (cardTypeList.contains(cardInfo.getCardId().getCardType())) {
                        cardsAndInfo.add(new CardInfoCardDTO(cardInfo, deck.getCards().get(i)));
                    }
                }
            }
        }

        LocalDate today = LocalDate.now().plusDays(1);
        cardsAndInfo = cardsAndInfo.stream()
                .filter(dto -> dto.getCardInfo().getNextRepetition().isBefore(today))
                .sorted(Comparator.comparing(dto -> dto.getCardInfo().getNextRepetition()))
                .collect(Collectors.toList());

        HashMap<Deck, Integer> dueDecks = new HashMap<>();

        for (CardInfoCardDTO cardInfoCardDTO : cardsAndInfo) {
            Deck currentDeck = cardInfoCardDTO.getCard().getDeck();
            if (!dueDecks.containsKey(currentDeck)) {
                dueDecks.put(currentDeck, 1);
            } else {
                dueDecks.put(currentDeck, dueDecks.get(currentDeck) + 1);
            }
        }
        return dueDecks;
    }

    private List<CardInfo> getUserCards(Usr usr, long deckId) {
        Optional<Deck> deck = deckRepository.findById(deckId);
        if (deck.isPresent()) {
            return cardInfoRepository.findCardInfoByDeckAndUser(deck.get(), usr);
        }
        return new ArrayList<>();
    }
}

package de.techfak.gse.template.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

/**
 * Implementation for FriendRequestService.
 */
@Service
public class FriendRequestServiceImpl implements FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserService userService;

    @Autowired
    public FriendRequestServiceImpl(FriendRequestRepository friendRequestRepository, UserService userService) {
        this.friendRequestRepository = friendRequestRepository;
        this.userService = userService;
    }

    @Override
    public void acceptRequest(FriendRequest request) {
        request.setStatus(FriendRequest.Status.ACCEPTED);
        friendRequestRepository.save(request);
    }

    @Override
    public void declineRequest(FriendRequest request) {
        request.setStatus(FriendRequest.Status.DECLINED);
        friendRequestRepository.save(request);
    }

    @Override
    public List<FriendRequest> getPendingRequests(Usr recipient) {
        return friendRequestRepository.findByRecipientAndStatus(recipient, FriendRequest.Status.PENDING);
    }

    @Override
    public FriendRequest sendRequestByRecipientEmail(Usr requester, String recipientEmail) {
        Usr recipient = userService.loadUserByUsername(recipientEmail);
        if (friendRequestRepository.existsByRequesterAndRecipient(requester, recipient)) {
            throw new IllegalStateException("Die Anfrage existiert schon");
        }
        if (friendRequestRepository.existsByRequesterAndRecipient(recipient, requester)) {
            throw new IllegalStateException("Die Anfrage existiert schon zwischen Users");
        }

        FriendRequest request = new FriendRequest(requester, recipient);
        return friendRequestRepository.save(request);
    }

    /**
     * Saves and gets Friends of a User.
     *
     * @param user
     * @return
     */
    public Set<Usr> getFriends(Usr user) {
        List<FriendRequest> acceptedAsRequester = friendRequestRepository.
                findByRequesterAndStatus(user, FriendRequest.Status.ACCEPTED);
        List<FriendRequest> acceptedAsRecipient = friendRequestRepository.
                findByRecipientAndStatus(user, FriendRequest.Status.ACCEPTED);
        Set<Usr> friends = new LinkedHashSet<>();
        for (FriendRequest fr : acceptedAsRequester) {
            friends.add(fr.getRecipient());
        }
        for (FriendRequest fr : acceptedAsRecipient) {
            friends.add(fr.getRequester());
        }
        return friends;
    }
    @Override
    public Optional<FriendRequest> findById(Long id) {
        return friendRequestRepository.findById(id);
    }
}

package de.techfak.gse.template.domain;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service for Friendrequest.
 */
public interface FriendRequestService {

    void acceptRequest(FriendRequest request);

    void declineRequest(FriendRequest request);

    List<FriendRequest> getPendingRequests(Usr recipient);

    FriendRequest sendRequestByRecipientEmail(Usr requester, String recipientEmail);

    Set<Usr> getFriends(Usr user);

    Optional<FriendRequest> findById(Long id);
}

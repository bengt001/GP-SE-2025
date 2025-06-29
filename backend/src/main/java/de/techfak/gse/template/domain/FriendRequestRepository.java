package de.techfak.gse.template.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends CrudRepository<FriendRequest, Long> {

    List<FriendRequest> findByRecipientAndStatus(Usr recipient, FriendRequest.Status status);

    List<FriendRequest> findByRequesterAndStatus(Usr requester, FriendRequest.Status status);

    List<FriendRequest> findByRequester(Usr requester);

    Optional<FriendRequest> findByRequesterAndRecipient(Usr requester, Usr recipient);

    boolean existsByRequesterAndRecipient(Usr requester, Usr recipient);
}

package de.techfak.gse.template.domain.repositories;

import de.techfak.gse.template.domain.entities.FriendRequest;
import de.techfak.gse.template.domain.entities.Usr;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Friendrequest.
 */
public interface FriendRequestRepository extends CrudRepository<FriendRequest, Long> {

    List<FriendRequest> findByRecipientAndStatus(Usr recipient, FriendRequest.Status status);

    List<FriendRequest> findByRequesterAndStatus(Usr requester, FriendRequest.Status status);

    List<FriendRequest> findByRequester(Usr requester);

    Optional<FriendRequest> findByRequesterAndRecipient(Usr requester, Usr recipient);

    boolean existsByRequesterAndRecipient(Usr requester, Usr recipient);
}

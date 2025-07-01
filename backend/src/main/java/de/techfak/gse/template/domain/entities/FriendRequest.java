package de.techfak.gse.template.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Friend request between two users.
 */
@Entity
@Getter
@Setter
public class
FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    private Usr requester;

    @ManyToOne(optional = false)
    private Usr recipient;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private LocalDate createdAt = LocalDate.now();

    /**
     * Status of a Friend request.
     */
    public enum Status {
        PENDING,
        ACCEPTED,
        DECLINED
    }
    protected FriendRequest() {

    }

    /**
     * Creates a new Friend request.
     *
     * @param requester
     * @param recipient
     */
    public FriendRequest(Usr requester, Usr recipient) {
        this.requester = requester;
        this.recipient = recipient;
        this.status = Status.PENDING;
        this.createdAt = LocalDate.now();
    }
}


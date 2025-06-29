package de.techfak.gse.template.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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

    public enum Status {
        PENDING,
        ACCEPTED,
        DECLINED
    }
    protected FriendRequest() {

    }

    public FriendRequest(Usr requester, Usr recipient) {
        this.requester = requester;
        this.recipient = recipient;
        this.status = Status.PENDING;
        this.createdAt = LocalDate.now();
    }
}


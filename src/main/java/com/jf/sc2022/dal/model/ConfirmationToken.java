package com.jf.sc2022.dal.model;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "confirmation_tokens")
public class ConfirmationToken {
    @Id
    @GeneratedValue
    private long   tokenId;
    private String token;
    private Date   dateCreated;
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id", nullable = false)
    private User   user;

    public ConfirmationToken(final User user) {
        this.user   = user;
        dateCreated = new Date();
        token       = UUID.randomUUID().toString();
    }
}

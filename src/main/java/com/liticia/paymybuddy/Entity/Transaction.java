package com.liticia.paymybuddy.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "transactione")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private float amount;

    private String subject;

    private Date transactionDate;

    private float debitedAmount;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "friend_user", referencedColumnName = "id")
    User user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "principal_user", referencedColumnName = "id")
    private User principalUser;
}

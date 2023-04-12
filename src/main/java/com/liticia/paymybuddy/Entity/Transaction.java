package com.liticia.paymybuddy.Entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private Double amount;

    private String subject;

    private Date transactionDate;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "contact_id", referencedColumnName = "contact_id")
    Contact contact;
}

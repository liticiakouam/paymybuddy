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
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private float amount;

    private String subject;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date transactionDate;

    private float debitedAmount;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "contact_id", referencedColumnName = "contact_id")
    Contact contact;
}

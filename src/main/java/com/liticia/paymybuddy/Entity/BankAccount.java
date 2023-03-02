package com.liticia.paymybuddy.Entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "bankaccount")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private  int userId = 1;

    private boolean status = true;

    @NotEmpty
    @Column(nullable = false)
    private String accountNumber;

    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
}

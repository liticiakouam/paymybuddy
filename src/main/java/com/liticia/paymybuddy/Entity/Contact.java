package com.liticia.paymybuddy.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long idPrincipalUser;
    private long idContactUser;
    private Date date;
}

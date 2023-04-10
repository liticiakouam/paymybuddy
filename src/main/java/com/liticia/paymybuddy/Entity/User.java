package com.liticia.paymybuddy.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import java.util.Date;

@Data
@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
public class User {

    @Id
    private long id;

    private String firstname;

    private String lastname;

    @Email(message = "{errors.invalid_email}")
    private String email;

    private String password;

    private Double balance;

    private Date createdAt;

    private Date updatedAt;
}
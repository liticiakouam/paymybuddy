package com.liticia.paymybuddy.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.*;

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

    private float balance;

    private Date createdAt;

    private Date updatedAt;

    @ManyToMany(fetch = FetchType.EAGER, cascade= CascadeType.ALL)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private List<Role> roles = new ArrayList<>();
}
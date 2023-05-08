package com.liticia.paymybuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @Length(min = 4, message = "the minimum value is 7")
    @Column(unique = true)
    private String firstname;

    @Length(min = 4, message = "the minimum value is 3")
    private String lastname;

    @Email(message = "please enter a valid address email")
    private String email;

    @Length(min = 5, message = "the minimum value is 5")
    private String password;

}

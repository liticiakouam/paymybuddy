package com.liticia.paymybuddy.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class BankAccountCreate {
    @Length(min = 7, message = "the minimum value is 7")
    @NotBlank(message = "please fill the column")
    @Column(unique=true)
    private String accountNumber;

    @Length(min = 3, message = "the minimum value is 3")
    @NotBlank(message = "please fill the column")
    private String description;
}

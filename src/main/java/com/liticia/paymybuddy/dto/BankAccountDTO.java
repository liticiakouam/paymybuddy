package com.liticia.paymybuddy.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
public class BankAccountDTO {

    private int id;

    private  int userId;

    private String accountNumber;

    private Boolean active;

    private String description;

    private Date createdAt;
}

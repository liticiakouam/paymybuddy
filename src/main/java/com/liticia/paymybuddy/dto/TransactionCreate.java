package com.liticia.paymybuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@NotNull
public class TransactionCreate {
    private long contactId;
    private String subject;
    private float amount;
}

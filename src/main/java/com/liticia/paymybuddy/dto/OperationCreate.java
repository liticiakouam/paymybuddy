package com.liticia.paymybuddy.dto;

import com.liticia.paymybuddy.Entity.OperationType;
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
public class OperationCreate {
    private OperationType operationType;
    private String accountNumber;
    private float amount;
}

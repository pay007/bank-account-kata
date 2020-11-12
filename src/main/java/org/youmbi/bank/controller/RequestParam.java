package org.youmbi.bank.controller;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@Data
@Builder
public class RequestParam {
    @NonNull
    private Long accountId;
    @NonNull
    private BigDecimal amount;
}

package org.youmbi.bank.controller;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class RequestParam {
    private String accountId;
    private String amount;
}

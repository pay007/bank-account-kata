package org.youmbi.bank.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class BalanceStatement {
    private Date date;
    private BigDecimal balance;
    private Long accountId;
    private BigDecimal amount;
}

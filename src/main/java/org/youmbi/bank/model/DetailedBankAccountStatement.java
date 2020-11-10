package org.youmbi.bank.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class DetailedBankAccountStatement {
    private String operation;
    private Date date;
    private BigDecimal amount;
}

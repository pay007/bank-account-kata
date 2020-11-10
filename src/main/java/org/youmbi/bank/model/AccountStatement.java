package org.youmbi.bank.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AccountStatement {
    private BalanceStatement balanceStatement;
    private List<OperationDetails> details;
}

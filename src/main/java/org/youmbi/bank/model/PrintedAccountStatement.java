package org.youmbi.bank.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PrintedAccountStatement {
    private CurrentBankAccountStatement summary;
    private List<DetailedBankAccountStatement> details;
}

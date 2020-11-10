package org.youmbi.bank.entity;

import java.math.BigDecimal;
import java.util.function.BiFunction;

public enum AccountEventType {
    DEPOSIT((balance, amount) -> balance.add(amount)),
    WITHDRAWAL((balance, amount) -> balance.subtract(amount));

    AccountEventType(BiFunction<BigDecimal, BigDecimal, BigDecimal> eventProcessor) {
        this.eventProcessor = eventProcessor;
    }

    BiFunction<BigDecimal, BigDecimal,BigDecimal> eventProcessor;

    /**
     * Each Event type provide his own processor,
     * @param balance
     * @param amount
     * @return
     */
    public BigDecimal process(BigDecimal balance, BigDecimal amount) {
        return eventProcessor.apply(balance,amount);
    }
}

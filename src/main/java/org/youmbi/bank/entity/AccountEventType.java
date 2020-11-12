package org.youmbi.bank.entity;

import java.math.BigDecimal;
import java.util.function.BiFunction;

public enum AccountEventType {
    DEPOSIT((balance, amount) -> balance.add(amount)),
    WITHDRAWAL((balance, amount) -> balance.subtract(amount));

    AccountEventType(BiFunction<BigDecimal, BigDecimal, BigDecimal> eventProcessor) {
        this.eventProcessor = eventProcessor;
    }

    BiFunction<BigDecimal, BigDecimal, BigDecimal> eventProcessor;

    /**
     * Each Event type is providing his own processor,
     *
     * @param balance the initial balance on which we will apply the amount (add or remove)
     * @param amount  the amount to add or remove based on event type (DEPOSIT or WITHDRAWAL)
     * @return
     */
    public BigDecimal process(BigDecimal balance, BigDecimal amount) {
        return eventProcessor.apply(balance, amount);
    }
}

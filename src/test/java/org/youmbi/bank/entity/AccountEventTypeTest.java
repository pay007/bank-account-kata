package org.youmbi.bank.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountEventTypeTest {

    @Test
    public void should_perform_addition_for_deposit_type() {
        Assertions.assertEquals(AccountEventType.DEPOSIT.process(BigDecimal.ONE, BigDecimal.ONE), BigDecimal.valueOf(2));
    }

    @Test
    public void should_perform_substraction_for_withdrawal_type() {
        Assertions.assertEquals(AccountEventType.WITHDRAWAL.process(BigDecimal.TEN, BigDecimal.TEN), BigDecimal.ZERO);
    }

}
package org.youmbi.bank.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.youmbi.bank.EsConfiguration;
import org.youmbi.bank.entity.AccountEvent;
import org.youmbi.bank.model.CurrentBankAccountStatement;
import org.youmbi.bank.model.DetailedBankAccountStatement;
import org.youmbi.bank.model.PrintedAccountStatement;
import org.youmbi.bank.repository.BankAccountRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringJUnitConfig
@ContextConfiguration(classes = {EsConfiguration.class})
class BankAccountServiceTest {

    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @BeforeEach
    void setUp() {
        bankAccountRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        bankAccountRepository.deleteAll();
    }

    @Test
    void balance_should_be_400_when_deposit_400_and_previous_balance_was_0() {
        Long accountId = 0L;
        BigDecimal amount = BigDecimal.valueOf(400);
        BigDecimal expectedBalance = BigDecimal.valueOf(400);
        CurrentBankAccountStatement bankAccountStatement = bankAccountService.deposit(accountId, amount);
        Assertions.assertEquals(expectedBalance.intValue(), bankAccountStatement.getBalance().intValue());
        Assertions.assertEquals(amount.intValue(), bankAccountStatement.getAmount().intValue());
    }

    @Test
    void balance_should_be_minus100_when_deposit_400_and_withdrawal_500() {
        Long accountId = 1000L;
        BigDecimal deposit = BigDecimal.valueOf(400);
        BigDecimal expected = BigDecimal.valueOf(-100);
        BigDecimal withdrawal = BigDecimal.valueOf(500);
        bankAccountService.deposit(accountId, deposit);
        CurrentBankAccountStatement bankAccountStatement = bankAccountService.withdrawal(accountId, withdrawal);
        Assertions.assertEquals(expected.intValue(), bankAccountStatement.getBalance().intValue());
        Assertions.assertEquals(withdrawal.intValue(), bankAccountStatement.getAmount().intValue());
    }

    @Test
    void balance_should_be_1000_after_deposit_600_then_600_and_withdrawal_200() {
        Long accountId = 0L;
        BigDecimal expected = BigDecimal.valueOf(1000);
        bankAccountService.deposit(accountId, BigDecimal.valueOf(600));
        bankAccountService.deposit(accountId, BigDecimal.valueOf(600));
        bankAccountService.withdrawal(accountId, BigDecimal.valueOf(200));
        CurrentBankAccountStatement currentAccountStatement = bankAccountService.getCurrentAccountStatement(accountId);
        Assertions.assertEquals(expected.intValue(), currentAccountStatement.getBalance().intValue());
    }

    @Test
    void detailed_statement_should_be_printed_with_all_operations() {
        Long accountId = 0L;
        BigDecimal expected = BigDecimal.valueOf(800);
        bankAccountService.deposit(accountId, BigDecimal.valueOf(600));
        bankAccountService.withdrawal(accountId, BigDecimal.valueOf(200));
        bankAccountService.deposit(accountId, BigDecimal.valueOf(500));
        bankAccountService.withdrawal(accountId, BigDecimal.valueOf(100));
        PrintedAccountStatement detailedStatement = bankAccountService.printDetailedStatement(accountId);
        Assertions.assertEquals(expected.intValue(), detailedStatement.getSummary().getBalance().intValue());
        Assertions.assertEquals(4, detailedStatement.getDetails().size());
    }

}
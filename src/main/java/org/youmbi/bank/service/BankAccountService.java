package org.youmbi.bank.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.youmbi.bank.entity.AccountEvent;
import org.youmbi.bank.entity.AccountEventType;
import org.youmbi.bank.exception.BankAccountException;
import org.youmbi.bank.model.BalanceStatement;
import org.youmbi.bank.model.OperationDetails;
import org.youmbi.bank.model.AccountStatement;
import org.youmbi.bank.repository.BankAccountRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BankAccountService {

    private BankAccountRepository bankAccountRepository;

    @Autowired
    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    /**
     * Service to perform deposit on account
     * @param accountId account id
     * @param amount amount of the deposit
     * @return
     */
    public BalanceStatement deposit(final Long accountId, final BigDecimal amount) throws BankAccountException {
        // create a deposit event
        AccountEvent event = buildAccountEvent(accountId, amount, AccountEventType.DEPOSIT);
        // persist the DEPOSIT event
        save(event);
        // query all events using order
        List<AccountEvent> accountEvents = getEventsByAccountId(accountId);
        // Reduce events to determine current account Balance
        return getBalance(accountId, amount, accountEvents);
    }

    /**
     * For simplicity there is no Overdraft limit
     *
     * @param accountId
     * @param amount
     * @return
     * @throws BankAccountException
     */
    public BalanceStatement withdrawal(final Long accountId, final BigDecimal amount) throws BankAccountException {
        // create WITHDRAWAL event
        AccountEvent event = buildAccountEvent(accountId, amount, AccountEventType.WITHDRAWAL);
        // save WITHDRAWAL event
        save(event);
        // Reduce events to determine current account Balance
        List<AccountEvent> accountEvents = getEventsByAccountId(accountId);
        return getBalance(accountId, amount, accountEvents);
    }


    /**
     * build the {@link BalanceStatement} by computing current account balance
     * @param accountId
     * @param amount
     * @param accountEvents
     * @return
     */
    private BalanceStatement getBalance(Long accountId, BigDecimal amount, List<AccountEvent> accountEvents) {
        BigDecimal balance = computeBalance(accountEvents);
        return BalanceStatement.builder()
                .accountId(accountId)
                .amount(amount)
                .balance(balance)
                .date(new Date())
                .build();
    }


    /**
     * Service to print account statement with details of all operation
     * @param accountId
     * @return
     * @throws BankAccountException
     */
    public AccountStatement getAccountStatement(final Long accountId) throws BankAccountException {
        // query all event and print them order by date of event
        List<AccountEvent> accountEvents = getEventsByAccountId(accountId);
        // convert to DetailedBankAccountStatement
        BalanceStatement balanceStatement = getBalance(accountId, null, accountEvents);
        Function<AccountEvent, OperationDetails> converter = (event) -> OperationDetails.builder()
                .amount(event.getAmount())
                .date(event.getEventDate())
                .operation(event.getEventType().toString())
                .build();
        List<OperationDetails> details = accountEvents.stream().map(converter).collect(Collectors.toList());

        return AccountStatement.builder()
                .details(details)
                .balanceStatement(balanceStatement)
                .build();
    }

    /**
     * Service to aggregate Events and compute account balance
     *
     * @param accountId
     * @return {@link BalanceStatement} aggregate representation
     */
    public BalanceStatement getBalanceStatement(final Long accountId) {
        // query all events using order
        List<AccountEvent> accountEvents = getEventsByAccountId(accountId);
        // Reduce events to determine current account Balance
        return getBalance(accountId, BigDecimal.ONE, accountEvents);
    }

    /**
     * Reduce/aggregate collection of events by applying suitable
     * operation based on eventType {@link AccountEventType}
     *
     * @param accountEvents list of {@link AccountEvent}
     * @return balance {@link BigDecimal}BigDecimal
     */
    private BigDecimal computeBalance(List<AccountEvent> accountEvents) {
        BigDecimal balance = accountEvents.stream().
                reduce(BigDecimal.ZERO, (acc, event) -> event.processEvent(acc), BigDecimal::add);
        return balance;
    }


    /**
     * lookup event by account ID
     *
     * @param accountId bank account ID
     * @return list of {@link AccountEvent} matching accountId
     */
    private List<AccountEvent> getEventsByAccountId(Long accountId) {
        return bankAccountRepository.findByAccountId(accountId);
    }

    /**
     * Save Event into the Event storage
     *
     * @param event
     */
    private void save(AccountEvent event) {
        bankAccountRepository.save(event);
    }

    /**
     * Builder to create {@link AccountEvent}
     *
     * @param accountId bank account id
     * @param amount    amount
     * @param eventType
     * @return
     */
    private AccountEvent buildAccountEvent(Long accountId, BigDecimal amount, AccountEventType eventType) {
        AccountEvent event = AccountEvent.builder()
                .accountId(accountId)
                .eventDate(new Date())
                .eventType(eventType)
                .amount(amount).build();
        return event;
    }


}

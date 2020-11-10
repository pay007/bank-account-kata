package org.youmbi.bank.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.youmbi.bank.exception.BankAccountException;
import org.youmbi.bank.model.DetailedBankAccountStatement;
import org.youmbi.bank.model.PrintedAccountStatement;
import org.youmbi.bank.service.BankAccountService;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping(path = "/bank")
public class BankAccountControlller {

    private BankAccountService bankAccountService;

    @Autowired
    public BankAccountControlller(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    /**
     * Return details of bank account statement as a list of {@link DetailedBankAccountStatement} which will
     * be displayed to customer
     *
     * @param accountId account id
     * @return list of {@link DetailedBankAccountStatement}
     * @throws Exception
     */
    @RequestMapping(path = "/{accountId}/printstatement", method = RequestMethod.GET)
    public ResponseEntity<PrintedAccountStatement> printStatement(@PathVariable("accountId") String accountId) throws Exception {
        assert accountId != null;
        return Optional.ofNullable(bankAccountService.printDetailedStatement(Long.valueOf(accountId)))
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> BankAccountException.operationFailed("Print statement operation failed"));
    }


    /**
     * Apply deposit of specified amount on specified account ID
     *
     * @param accountId account Id on which deposit is performed
     * @param amount    deposit amount
     * @return
     */
    @RequestMapping(path = "/{accountId}/deposit/{amount}", method = RequestMethod.POST)
    public ResponseEntity deposit(@PathVariable("accountId") String accountId, @PathVariable("amount") String amount) {
        assert accountId != null;
        assert amount != null;
        return Optional.ofNullable(bankAccountService.deposit(Long.valueOf(accountId), BigDecimal.valueOf(Long.valueOf(amount))))
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> BankAccountException.operationFailed("Deposit operation failed"));
    }

    /**
     * Apply Withdrawal of specified amount on specified account ID
     *
     * @param accountId account Id on which deposit is performed
     * @param amount    withdrawal amount
     * @return
     */
    @RequestMapping(path = "/{accountId}/withdrawal/{amount}", method = RequestMethod.GET)
    public ResponseEntity withdrawal(@PathVariable("accountId") String accountId, @PathVariable("amount") String amount) {
        assert accountId != null;
        assert amount != null;
        return Optional.ofNullable(bankAccountService.withdrawal(Long.valueOf(accountId), BigDecimal.valueOf(Long.valueOf(amount))))
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> BankAccountException.operationFailed("Withdrawal operation failed"));
    }


}

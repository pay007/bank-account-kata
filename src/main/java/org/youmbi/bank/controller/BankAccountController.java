package org.youmbi.bank.controller;


import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.youmbi.bank.exception.BankAccountException;
import org.youmbi.bank.model.AccountStatement;
import org.youmbi.bank.model.BalanceStatement;
import org.youmbi.bank.model.OperationDetails;
import org.youmbi.bank.service.BankAccountService;

import java.util.Optional;

@RestController
@RequestMapping(path = "/bank")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @Autowired
    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    /**
     * Return details of bank account statement as a list of {@link OperationDetails} which will
     * be displayed to customer
     *
     * @param accountId account id
     * @return list of {@link OperationDetails}
     * @throws Exception
     */
    @RequestMapping(path = "/printstatement/{accountId}", method = RequestMethod.GET)
    public ResponseEntity<AccountStatement> printStatement(@PathVariable("accountId") final String accountId) throws BankAccountException {
        assert accountId != null;
        return Optional.ofNullable(bankAccountService.getAccountStatement(Long.valueOf(accountId)))
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    /**
     * Apply deposit of specified amount on specified account ID
     *
     * @param requestParam {@link RequestParam}
     * @return
     * @throws BankAccountException
     */
    @RequestMapping(path = "/deposit", method = RequestMethod.POST)
    public ResponseEntity<BalanceStatement> deposit(@RequestBody @NonNull final RequestParam requestParam) throws BankAccountException {
        return Optional.ofNullable(bankAccountService.deposit(requestParam.getAccountId(), requestParam.getAmount()))
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Apply Withdrawal of specified amount on specified account ID
     *
     * @param requestParam {@link RequestParam}
     * @return
     * @throws BankAccountException
     */
    @RequestMapping(path = "/withdrawal", method = RequestMethod.POST)
    public ResponseEntity<BalanceStatement> withdrawal(@RequestBody @NonNull final RequestParam requestParam) throws BankAccountException {
        return Optional.ofNullable(bankAccountService.withdrawal(requestParam.getAccountId(), requestParam.getAmount()))
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}

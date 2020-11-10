package org.youmbi.bank.exception;

public class BankAccountException extends RuntimeException {

    public BankAccountException(String message) {
        super(message);
    }

    public  static BankAccountException invalidRequest(String message){
        return new BankAccountException(message);
    }

    public  static BankAccountException operationFailed(String message){
        return new BankAccountException(message);
    }
}

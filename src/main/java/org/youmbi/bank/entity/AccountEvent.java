package org.youmbi.bank.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.Timestamp;
import java.util.Date;
import java.util.UUID;

@Document(indexName = "accountevent")
@ToString
@Data
@Builder

/**
 * Each operation perform on bank account is manage as Event
 * This Document representing each event
 * We are supporting 2 type {@link AccountEventType} of event/
 *  - DEPOSIT (event which increment account balance)
 *  - WITHDRAWAL (event which decrement account balance)
 *
 *  Events are processed based on their type
 */
public class AccountEvent implements Serializable {
    @Id
    private final String eventId = UUID.randomUUID().toString();
    @CreatedDate
    private Date eventDate;
    private Long accountId;
    private AccountEventType eventType;
    private BigDecimal amount;

    public BigDecimal processEvent(final BigDecimal balance){
        return this.eventType.process(balance,this.amount);
    }

}

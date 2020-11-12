package org.youmbi.bank.entity;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Document(indexName = "accountevent")
@ToString
@Data
@Builder

/**
 * Each operation performed on bank account is managed as Event
 * This Document is representing each event
 * We are supporting 2 types {@link AccountEventType} of event
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

    public BigDecimal processEvent(final BigDecimal balance) {
        return this.eventType.process(balance, this.amount);
    }

}

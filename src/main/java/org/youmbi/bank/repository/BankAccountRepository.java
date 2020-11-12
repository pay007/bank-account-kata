package org.youmbi.bank.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.youmbi.bank.entity.AccountEvent;

import java.util.Set;

@Repository
public interface BankAccountRepository extends ElasticsearchRepository<AccountEvent, String> {
    Set<AccountEvent> findByAccountId(Long accountId);
}

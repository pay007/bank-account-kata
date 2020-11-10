package org.youmbi.bank.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.youmbi.bank.entity.AccountEvent;

import java.util.List;

@Repository
public interface BankAccountRepository extends ElasticsearchRepository<AccountEvent, String> {
    List<AccountEvent> findByAccountId(Long accountId);
}

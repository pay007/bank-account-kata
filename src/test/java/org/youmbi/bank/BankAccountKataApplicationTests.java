package org.youmbi.bank;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {EsConfiguration.class,BankAccountKataApplication.class})
class BankAccountKataApplicationTests {

    @Test
    void contextLoads() {
    }

}

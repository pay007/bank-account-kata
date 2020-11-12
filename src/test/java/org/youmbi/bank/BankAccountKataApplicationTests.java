package org.youmbi.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.youmbi.bank.controller.BankAccountController;
import org.youmbi.bank.controller.RequestParam;
import org.youmbi.bank.exception.BankAccountException;
import org.youmbi.bank.model.BalanceStatement;
import org.youmbi.bank.service.BankAccountService;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Test controller layer without loading the whole context
 * This test is using a Mock bank Account service instance
 */
@WebMvcTest(BankAccountController.class)
class BankAccountKataApplicationTests {

    public static final String DEPOSIT_URL = "/bank/deposit";
    public static final String WITHDRAWAL_URL = "/bank/withdrawal";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BankAccountService bankAccountService;

    @Test
    void deposit_endpoint_should_return_status_200() throws Exception {
        long accountId = 100L;
        BigDecimal amount = BigDecimal.valueOf(5000L, 1);
        BigDecimal balance = BigDecimal.valueOf(5000L, 1);
        RequestParam requestParam = RequestParam.builder()
                .accountId(accountId)
                .amount(amount)
                .build();

        BalanceStatement expected = BalanceStatement.builder()
                .accountId(accountId)
                .amount(amount)
                .date(new Date())
                .balance(balance).build();

        Mockito.when(bankAccountService.deposit(accountId, amount)).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.post(DEPOSIT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestParam)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .equals(MockMvcResultMatchers.content().equals(Matchers.equalTo(expected)));

    }

    @Test
    void withdrawal_endpoint_should_return_status_200() throws Exception {
        long accountId = 100L;
        BigDecimal amount = BigDecimal.valueOf(5000L, 1);
        BigDecimal balance = BigDecimal.valueOf(-5000L, 1);
        RequestParam requestParam = RequestParam.builder()
                .accountId(accountId)
                .amount(amount)
                .build();

        BalanceStatement expected = BalanceStatement.builder()
                .accountId(accountId)
                .amount(amount)
                .date(new Date())
                .balance(balance).build();

        Mockito.when(bankAccountService.withdrawal(accountId, amount)).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.post(WITHDRAWAL_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestParam)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .equals(MockMvcResultMatchers.content().equals(Matchers.equalTo(expected)));

    }

    @Test
    void withdrawal_endpoint_should_return_status_404_when_service_return_null() throws Exception {
        long accountId = 100L;
        BigDecimal amount = BigDecimal.valueOf(5000L, 1);
        BigDecimal balance = BigDecimal.valueOf(-5000L, 1);
        RequestParam requestParam = RequestParam.builder()
                .accountId(accountId)
                .amount(amount)
                .build();
        Mockito.when(bankAccountService.withdrawal(Mockito.any(), Mockito.any())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post(WITHDRAWAL_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestParam)))
                .equals(MockMvcResultMatchers.status().equals(Matchers.equalTo(HttpStatus.NOT_FOUND)));
    }

    @Test
    void deposit_endpoint_should_return_status_404_when_service_return_null() throws Exception {
        long accountId = 100L;
        BigDecimal amount = BigDecimal.valueOf(5000L, 1);
        BigDecimal balance = BigDecimal.valueOf(-5000L, 1);
        RequestParam requestParam = RequestParam.builder()
                .accountId(accountId)
                .amount(amount)
                .build();
        Mockito.when(bankAccountService.deposit(Mockito.any(), Mockito.any())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post(DEPOSIT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestParam)))
                .equals(MockMvcResultMatchers.status().equals(Matchers.equalTo(HttpStatus.NOT_FOUND)));
    }

    @Test
    void withdrawal_endpoint_should_return_status_400_for_null_param() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(WITHDRAWAL_URL)
                .contentType(MediaType.APPLICATION_JSON)
                // passing null as param should result to client error (4xx)
                .content(objectMapper.writeValueAsString(null)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }

    @Test
    void deposit_endpoint_should_return_status_400_for_null_param() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(DEPOSIT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                // passing null as param should result to client error (4xx)
                .content(objectMapper.writeValueAsString(null)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

}

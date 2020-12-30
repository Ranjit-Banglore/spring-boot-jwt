package de.infinity.jwt.resource;

import de.infinity.jwt.dto.CreateAccountDto;
import de.infinity.jwt.dto.DepositRequestDto;
import de.infinity.jwt.dto.IbanDto;
import de.infinity.jwt.dto.TransferDto;
import de.infinity.jwt.enumeration.AccountType;
import de.infinity.jwt.security.JpaUserDetailsService;
import de.infinity.jwt.service.BankAccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = BankAccountResource.class)
@AutoConfigureMockMvc(addFilters = false)
public class BankAccountResourceTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private BankAccountService bankAccountService;

    @MockBean
    private JpaUserDetailsService jpaUserDetailsService;

    @Test
    public void verifyThatBankAccountByCustomerIdIsInvoked() throws Exception {
        when(bankAccountService.getBankAccountByCustomerId(1L, null)).thenReturn(
                new ArrayList<>()
        );
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/1/bankaccounts")
                        .accept(MediaTypes.HAL_JSON_VALUE)
        ).andReturn();
        verify(bankAccountService).getBankAccountByCustomerId(1L, null);
        verifyNoMoreInteractions(bankAccountService);
    }

    @Test
    public void verifyThatDepositIsCalled() throws Exception {
        DepositRequestDto depositRequestDto = new DepositRequestDto("DE123456", 714.00);
        doNothing().when(bankAccountService).deposit(isA(DepositRequestDto.class));
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/deposit").content(convertToJson(depositRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        verify(bankAccountService).deposit(any());
    }

    @Test
    public void verifyThatTransferIsInvoked() throws Exception {
        TransferDto transferDto = new TransferDto("DE123456", "DE6564", 714.00);
        doNothing().when(bankAccountService).transfer(isA(Long.class), isA(TransferDto.class));
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/1/transfer").content(convertToJson(transferDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        verify(bankAccountService).transfer(any(), any());
    }

    @Test
    public void shouldInvokeCheckBalanceEndpoint() throws Exception {
        when(bankAccountService.checkBalance(1L, "DE123456")).thenReturn(987.0);
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/1/DE123456/balance")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        Assertions.assertEquals(mvcResult.getResponse().getContentAsString(), "987.0");
        verify(bankAccountService).checkBalance(1L, "DE123456");
    }

    @Test
    public void shouldInvokeCreateAccountEndpoint() throws Exception {
        CreateAccountDto createAccountDto = new CreateAccountDto();
        createAccountDto.setAccountType(AccountType.SAVINGS);
        doNothing().when(bankAccountService).createAccount(isA(Long.class), isA(CreateAccountDto.class));
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/1/bankaccount/create")
                        .content(convertToJson(createAccountDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful()).andReturn();
        verify(bankAccountService).createAccount(any(), any());
    }


    @Test
    public void shouldInvokeDeactivateEndpoint() throws Exception {
        IbanDto ibanDto = new IbanDto();
        ibanDto.setIban("DE123456789");
        doNothing().when(bankAccountService).changeAccountStatus(isA(Long.class), isA(IbanDto.class), isA(Boolean.class));
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/1/bankaccount/deactivate")
                        .content(convertToJson(ibanDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        verify(bankAccountService).changeAccountStatus(any(), any(), any());
    }

    @Test
    public void shouldInvokeActivateEndpoint() throws Exception {
        IbanDto ibanDto = new IbanDto();
        ibanDto.setIban("DE123456789");
        doNothing().when(bankAccountService).changeAccountStatus(isA(Long.class), isA(IbanDto.class), isA(Boolean.class));
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/1/bankaccount/activate")
                        .content(convertToJson(ibanDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        verify(bankAccountService).changeAccountStatus(any(), any(), any());
    }

    @Test
    public void shouldInvokeTransactionsEndpoint() throws Exception {
        when(bankAccountService.getTransactionHistory("DE123456")).thenReturn(
                new ArrayList<>()
        );
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/DE123456/transactions")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        verify(bankAccountService).getTransactionHistory("DE123456");
    }

    private String convertToJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

}

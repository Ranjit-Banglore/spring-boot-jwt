package de.infinity.jwt.integration;

import de.infinity.jwt.JwtApplication;
import de.infinity.jwt.dto.*;
import de.infinity.jwt.enumeration.AccountType;
import de.infinity.jwt.model.BankAccount;
import de.infinity.jwt.model.TransactionHistory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = JwtApplication.class
)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql("classpath:db/test-data.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BankAccountServiceIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void shouldReturnBankAccountDataForCustomer() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/1/bankaccounts")
                        .accept(MediaTypes.HAL_JSON_VALUE)
        ).andReturn();
        BankAccountDto[] response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BankAccountDto[].class);
        assertEquals(response.length, 3);
        assertEquals(response[0].getIban(), "DE59578454054765551413");
        assertEquals(response[1].getIban(), "DE96514388914485185983");
        assertEquals(response[2].getIban(), "DE21682132570987500492");
        assertEquals(response[0].getType(), AccountType.CHECKINGS);
        assertEquals(response[1].getType(), AccountType.PRIVATE_LOAN);
        assertEquals(response[2].getType(), AccountType.SAVINGS);
        assertEquals(response[0].getBalance(), 1231.0);
        assertEquals(response[1].getBalance(), 82421.0);
        assertEquals(response[2].getBalance(), 21731.0);
    }


    @Test
    public void shouldIncreaseBalanceAfterDeposit() throws Exception {
        DepositRequestDto depositRequestDto = new DepositRequestDto("DE59578454054765551413", 714.0);
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/deposit").content(objectMapper.writeValueAsString(depositRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        var actual = entityManager.createNamedQuery(BankAccount.NQ_GET_BY_IBAN, BankAccount.class)
                .setParameter("iban", "DE59578454054765551413")
                .getSingleResult();

        assertEquals(actual.getBalance(), 1945.0);

    }

    @Test
    public void shouldTransferMoneySuccessfully() throws Exception {
        TransferDto transferDto = new TransferDto("DE59578454054765551413", "DE96514388914485185983", 1000.00);
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/1/transfer").content(objectMapper.writeValueAsString(transferDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        var fromAccount = entityManager.createNamedQuery(BankAccount.NQ_GET_BY_IBAN, BankAccount.class)
                .setParameter("iban", "DE59578454054765551413")
                .getSingleResult();
        var toAccount = entityManager.createNamedQuery(BankAccount.NQ_GET_BY_IBAN, BankAccount.class)
                .setParameter("iban", "DE96514388914485185983")
                .getSingleResult();

        assertEquals(fromAccount.getBalance(), 231.0);
        assertEquals(toAccount.getBalance(), 83421.0);
    }

    @Test
    public void shouldCheckBalance() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/1/DE59578454054765551413/balance")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        assertEquals(mvcResult.getResponse().getContentAsString(), "1231.0");
    }

    @Test
    public void shouldCreateNewBankAccount() throws Exception {
        CreateAccountDto createAccountDto = new CreateAccountDto(AccountType.SAVINGS);
        var bankAccounts = entityManager.createNamedQuery(BankAccount.NQ_GET_BY_CUSTOMER_ID, BankAccountDto.class)
                .setParameter("customerId", 1L)
                .getResultList();
        //validate number of existing bankaccounts before API call
        assertEquals(bankAccounts.size(), 3);
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/1/bankaccount/create")
                        .content(objectMapper.writeValueAsString(createAccountDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful()).andReturn();

        bankAccounts = entityManager.createNamedQuery(BankAccount.NQ_GET_BY_CUSTOMER_ID, BankAccountDto.class)
                .setParameter("customerId", 1L)
                .getResultList();
        //confirms a new account is created and added
        assertEquals(bankAccounts.size(), 4);

    }

    @Test
    public void shouldDeactivateBankAccount() throws Exception {
        IbanDto ibanDto = new IbanDto("DE21682132570987500492");
        var before = entityManager.createNamedQuery(BankAccount.NQ_GET_BY_IBAN, BankAccount.class)
                .setParameter("iban", "DE21682132570987500492")
                .getSingleResult();
        //initially account is active
        assertTrue(before.getActive());
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/1/bankaccount/deactivate")
                        .content(objectMapper.writeValueAsString(ibanDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        var after = entityManager.createNamedQuery(BankAccount.NQ_GET_BY_IBAN, BankAccount.class)
                .setParameter("iban", "DE21682132570987500492")
                .getSingleResult();
        //Account is deactivated after API call
        assertFalse(after.getActive());
    }

    @Test
    public void shouldActivateBankAccount() throws Exception {
        IbanDto ibanDto = new IbanDto("DE21682132570987572137");
        var before = entityManager.createNamedQuery(BankAccount.NQ_GET_BY_IBAN, BankAccount.class)
                .setParameter("iban", "DE21682132570987572137")
                .getSingleResult();
        //initially account is active
        assertFalse(before.getActive());
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/2/bankaccount/activate")
                        .content(objectMapper.writeValueAsString(ibanDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        var after = entityManager.createNamedQuery(BankAccount.NQ_GET_BY_IBAN, BankAccount.class)
                .setParameter("iban", "DE21682132570987572137")
                .getSingleResult();
        //Account is deactivated after API call
        assertTrue(after.getActive());
    }

    @Test
    public void shouldReturnTransactionsHistory() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/DE59578454054765551413/transactions")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        TransactionHistory[] response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TransactionHistory[].class);
        //total number of transactions for account DE59578454054765551413
        assertEquals(response.length, 5);
    }


}

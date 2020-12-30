package de.infinity.jwt.service;

import de.infinity.jwt.dto.*;
import de.infinity.jwt.enumeration.AccountType;
import de.infinity.jwt.enumeration.TransactionType;
import de.infinity.jwt.exception.InvalidOperationException;
import de.infinity.jwt.model.BankAccount;
import de.infinity.jwt.model.Customer;
import de.infinity.jwt.model.TransactionHistory;
import de.infinity.jwt.repository.BankAccountRepository;
import de.infinity.jwt.repository.CustomerRepository;
import de.infinity.jwt.repository.TransactionHistoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BankAccountServiceTest {

    @InjectMocks
    private BankAccountService bankAccountService;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;

    @Test
    public void shouldReturnAllBankAccountForCustomer() {
        when(bankAccountRepository.getBankAccountByCustomerId(1L)).thenReturn(getBankAccountDto());
        var actual = bankAccountService.getBankAccountByCustomerId(1L, null);
        assertEquals(actual.size(), getBankAccountDto().size());
        verify(bankAccountRepository).getBankAccountByCustomerId(1L);
        verifyNoMoreInteractions(transactionHistoryRepository, bankAccountRepository, customerRepository);
    }

    @Test
    public void shouldOnlyReturnBankAccountWithType() {
        when(bankAccountRepository.getBankAccountByCustomerIdAndAccountType(1L, AccountType.SAVINGS)).thenReturn(List.of(new BankAccountDto(1L, "DE12345", AccountType.SAVINGS, 1213.0, Boolean.TRUE)));
        var actual = bankAccountService.getBankAccountByCustomerId(1L, AccountType.SAVINGS);
        assertEquals(actual.size(), 1);
        verify(bankAccountRepository).getBankAccountByCustomerIdAndAccountType(1L, AccountType.SAVINGS);
        verifyNoMoreInteractions(transactionHistoryRepository, bankAccountRepository, customerRepository);
    }

    @Test
    public void shouldDepositToBankAccount() {
        when(bankAccountRepository.getBankAccountByIban("DE12345")).thenReturn(new BankAccount(1L, "DE12345", AccountType.SAVINGS, 1213.0, Boolean.TRUE, new Customer()));
        doNothing().when(bankAccountRepository).persist(any());
        bankAccountService.deposit(new DepositRequestDto("DE12345", 100.0));
        verify(bankAccountRepository).getBankAccountByIban("DE12345");
        verify(bankAccountRepository, times(2)).persist(any());
        verifyNoMoreInteractions(transactionHistoryRepository, bankAccountRepository, customerRepository);
    }

    @Test
    public void shouldTransferFromBankAccount() {
        var fromBankaccount = new BankAccount(1L, "DE12345", AccountType.CHECKINGS, 1213.0, Boolean.TRUE, new Customer());
        var toBankaccount = new BankAccount(2L, "DE12345", AccountType.CHECKINGS, 1213.0, Boolean.TRUE, new Customer());
        when(bankAccountRepository.getBankAccountByCustomerIdAndIban(1L, "DE12345")).thenReturn(fromBankaccount);
        when(bankAccountRepository.getBankAccountByCustomerIdAndIban(1L, "DE12346")).thenReturn(toBankaccount);
        doNothing().when(bankAccountRepository).persist(any());
        bankAccountService.transfer(1L, new TransferDto("DE12345", "DE12346", 1000.0));
        assertEquals(fromBankaccount.getBalance(), 213.0);
        assertEquals(toBankaccount.getBalance(), 2213.0);
        verify(bankAccountRepository, times(2)).getBankAccountByCustomerIdAndIban(isA(Long.class), isA(String.class));
        verify(bankAccountRepository, times(4)).persist(any());
        verifyNoMoreInteractions(transactionHistoryRepository, bankAccountRepository, customerRepository);
    }

    @Test(expected = InvalidOperationException.class)
    public void shouldThrowExceptionWhenOperationIsNotAllowed() {
        var fromBankaccount = new BankAccount(1L, "DE12345", AccountType.PRIVATE_LOAN, 1213.0, Boolean.TRUE, new Customer());
        var toBankaccount = new BankAccount(2L, "DE12345", AccountType.SAVINGS, 1213.0, Boolean.TRUE, new Customer());
        when(bankAccountRepository.getBankAccountByCustomerIdAndIban(1L, "DE12345")).thenReturn(fromBankaccount);
        when(bankAccountRepository.getBankAccountByCustomerIdAndIban(1L, "DE12346")).thenReturn(toBankaccount);
        bankAccountService.transfer(1L, new TransferDto("DE12345", "DE12346", 1000.0));
    }

    @Test
    public void shouldCheckBalanceForAccount() {
        when(bankAccountRepository.getBalanceByCustomerIdAndIban(1L, "DE12345")).thenReturn(1000.0);
        var balance = bankAccountService.checkBalance(1L, "DE12345");
        assertEquals(balance, 1000.0);
        verify(bankAccountRepository).getBalanceByCustomerIdAndIban(1L, "DE12345");
        verifyNoMoreInteractions(transactionHistoryRepository, bankAccountRepository, customerRepository);
    }

    @Test
    public void shouldCreateBankAccountForAccountType() {
        when(customerRepository.getCustomerById(1L)).thenReturn(new Customer());
        doNothing().when(bankAccountRepository).persist(any());
        bankAccountService.createAccount(1L, new CreateAccountDto(AccountType.SAVINGS));
        verify(customerRepository).getCustomerById(1L);
        verify(bankAccountRepository).persist(any());
        verifyNoMoreInteractions(transactionHistoryRepository, bankAccountRepository, customerRepository);
    }

    @Test
    public void shouldActivateBankAccount() {
        var bankaccount = new BankAccount();
        when(bankAccountRepository.getBankAccountByCustomerIdAndIban(1L, "DE12345")).thenReturn(bankaccount);
        doNothing().when(bankAccountRepository).persist(any());
        bankAccountService.changeAccountStatus(1L, new IbanDto("DE12345"), Boolean.TRUE);
        assertTrue(bankaccount.getActive());
        verify(bankAccountRepository).getBankAccountByCustomerIdAndIban(1L, "DE12345");
        verify(bankAccountRepository).persist(any());
        verifyNoMoreInteractions(transactionHistoryRepository, bankAccountRepository, customerRepository);
    }

    @Test
    public void shouldDeactivateBankAccount() {
        var bankaccount = new BankAccount();
        when(bankAccountRepository.getBankAccountByCustomerIdAndIban(1L, "DE12345")).thenReturn(bankaccount);
        doNothing().when(bankAccountRepository).persist(any());
        bankAccountService.changeAccountStatus(1L, new IbanDto("DE12345"), Boolean.FALSE);
        assertFalse(bankaccount.getActive());
        verify(bankAccountRepository).getBankAccountByCustomerIdAndIban(1L, "DE12345");
        verify(bankAccountRepository).persist(any());
        verifyNoMoreInteractions(transactionHistoryRepository, bankAccountRepository, customerRepository);
    }

    @Test
    public void shouldVerifyTransactionHistory() {
        when(transactionHistoryRepository.getTransactionHistory("DE12345")).thenReturn(getTransactionHistory());
        var actual = bankAccountService.getTransactionHistory("DE12345");
        assertEquals(actual.size(), 3);
        verify(transactionHistoryRepository).getTransactionHistory("DE12345");
        verifyNoMoreInteractions(transactionHistoryRepository, bankAccountRepository, customerRepository);
    }


    private List<BankAccountDto> getBankAccountDto() {
        return List.of(new BankAccountDto(1L, "DE12345", AccountType.SAVINGS, 1213.0, Boolean.TRUE),
                new BankAccountDto(2L, "DE12345", AccountType.CHECKINGS, 2372.0, Boolean.TRUE),
                new BankAccountDto(3L, "DE12345", AccountType.PRIVATE_LOAN, 71824.0, Boolean.TRUE));
    }


    private List<TransactionHistory> getTransactionHistory() {
        return List.of(new TransactionHistory("DE12345", 1200.0, TransactionType.CREDIT, 3500.0),
                new TransactionHistory("DE12345", 1200.0, TransactionType.CREDIT, 2300.0),
                new TransactionHistory("DE12345", 1000.0, TransactionType.CREDIT, 1100.0));
    }
}

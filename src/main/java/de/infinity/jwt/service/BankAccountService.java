package de.infinity.jwt.service;

import de.infinity.jwt.dto.*;
import de.infinity.jwt.enumeration.AccountType;
import de.infinity.jwt.enumeration.TransactionType;
import de.infinity.jwt.exception.InvalidOperationException;
import de.infinity.jwt.model.BankAccount;
import de.infinity.jwt.model.TransactionHistory;
import de.infinity.jwt.repository.BankAccountRepository;
import de.infinity.jwt.repository.CustomerRepository;
import de.infinity.jwt.repository.TransactionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    public List<BankAccountDto> getBankAccountByCustomerId(Long customerId, AccountType filter) {
        List<BankAccountDto> bankAccountDtos;
        if (filter == null) {
            bankAccountDtos = bankAccountRepository.getBankAccountByCustomerId(customerId);
        } else {
            bankAccountDtos = bankAccountRepository.getBankAccountByCustomerIdAndAccountType(customerId, filter);
        }
        return bankAccountDtos;
    }

    public BankAccountDto getBankAccountById(Long customerId, Long id) {
        return bankAccountRepository.getBankAccountById(customerId, id);
    }

    @Transactional
    public void deposit(DepositRequestDto bankAccountRequestDto) {
        var bankAccount = bankAccountRepository.getBankAccountByIban(bankAccountRequestDto.getIban());
        var newBalance = bankAccount.getBalance() + bankAccountRequestDto.getAmount();
        bankAccount.setBalance(newBalance);
        bankAccountRepository.persist(new TransactionHistory(bankAccount.getIban(), bankAccountRequestDto.getAmount(), TransactionType.CREDIT, newBalance));
        bankAccountRepository.persist(bankAccount);
    }

    @Transactional
    public void transfer(Long customerId, TransferDto transferDto) {
        if (Objects.equals(transferDto.getFromIban(), transferDto.getToIban())) {
            throw new InvalidOperationException(String.format("Transfer to same account is not possible, to and from account must be different."));
        }
        var fromBankAccount = bankAccountRepository.getBankAccountByCustomerIdAndIban(customerId, transferDto.getFromIban());

        var toBankAccount = bankAccountRepository.getBankAccountByCustomerIdAndIban(customerId, transferDto.getToIban());

        if (isTransactionAllowedBetweenAccounts(fromBankAccount, toBankAccount)
                && isSufficientBalance(fromBankAccount, transferDto.getAmount())) {
            toBankAccount.setBalance(toBankAccount.getBalance() + transferDto.getAmount());
            fromBankAccount.setBalance(fromBankAccount.getBalance() - transferDto.getAmount());
            bankAccountRepository.persist(toBankAccount);
            bankAccountRepository.persist(fromBankAccount);
            bankAccountRepository.persist(new TransactionHistory(fromBankAccount.getIban(), transferDto.getAmount(), TransactionType.DEBIT, fromBankAccount.getBalance()));
            bankAccountRepository.persist(new TransactionHistory(toBankAccount.getIban(), transferDto.getAmount(), TransactionType.CREDIT, fromBankAccount.getBalance()));
        }
    }

    @Transactional
    public Double checkBalance(Long customerId, String iban) {
        return bankAccountRepository.getBalanceByCustomerIdAndIban(customerId, iban);
    }

    @Transactional
    public void createAccount(Long customerId, CreateAccountDto createAccountDto) {
        var customer = customerRepository.getCustomerById(customerId);
        var bankAccount = new BankAccount(createAccountDto.getAccountType(), customer);
        bankAccountRepository.persist(bankAccount);
    }

    @Transactional
    public void changeAccountStatus(Long customerId, IbanDto ibanDto, Boolean active) {
        var bankaccount = bankAccountRepository.getBankAccountByCustomerIdAndIban(customerId, ibanDto.getIban());
        bankaccount.setActive(active);
        bankAccountRepository.persist(bankaccount);
    }

    @Transactional
    public List<TransactionHistory> getTransactionHistory(String iban) {
        return transactionHistoryRepository.getTransactionHistory(iban);
    }

    private boolean isSufficientBalance(BankAccount fromBankAccount, Double amount) {
        if (amount > fromBankAccount.getBalance()) {
            throw new InvalidOperationException(String.format("Insufficient funds, you can only transfer upto %s Euro.", fromBankAccount.getBalance()));
        }
        return true;
    }

    private boolean isTransactionAllowedBetweenAccounts(BankAccount fromBankAccount, BankAccount toBankAccount) {
        if (fromBankAccount.getType() == AccountType.PRIVATE_LOAN) {
            throw new InvalidOperationException(String.format("Transfer from %s Account is not allowed", AccountType.PRIVATE_LOAN));
        } else if (fromBankAccount.getType() == AccountType.SAVINGS) {
            return validateForSavings(toBankAccount);
        } else {
            return true;
        }
    }

    private boolean validateForSavings(BankAccount toBankAccount) {
        if (toBankAccount.getType() != AccountType.CHECKINGS) {
            throw new InvalidOperationException(String.format("Transfer from %s Account to %s is not allowed", AccountType.SAVINGS, toBankAccount.getType()));
        }
        return true;
    }


}

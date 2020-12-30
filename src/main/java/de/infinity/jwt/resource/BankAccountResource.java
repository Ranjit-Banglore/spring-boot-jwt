package de.infinity.jwt.resource;

import de.infinity.jwt.dto.CreateAccountDto;
import de.infinity.jwt.dto.DepositRequestDto;
import de.infinity.jwt.dto.IbanDto;
import de.infinity.jwt.dto.TransferDto;
import de.infinity.jwt.enumeration.AccountType;
import de.infinity.jwt.helper.LinkBuilder;
import de.infinity.jwt.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
public class BankAccountResource {

    @Autowired
    private BankAccountService bankAccountService;

    @RolesAllowed("")
    @GetMapping(value = "/{customerId}/bankaccounts", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity getBankAccounts(@PathVariable Long customerId, @RequestParam(required = false) AccountType filter) {
        var list = bankAccountService.getBankAccountByCustomerId(customerId, filter);
        return ResponseEntity.ok(new LinkBuilder().build(list, customerId));
    }

    @GetMapping(value = "/{customerId}/bankaccounts/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity getBankAccount(@PathVariable Long customerId, @PathVariable Long id) {
        var bankAccountDto = bankAccountService.getBankAccountById(customerId, id);
        return ResponseEntity.ok(new LinkBuilder().build(bankAccountDto, customerId));
    }

    @PostMapping(value = "/deposit", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity deposit(@Valid @RequestBody DepositRequestDto requestDto) {
        bankAccountService.deposit(requestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{customerId}/transfer", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity transfer(@PathVariable Long customerId, @Valid @RequestBody TransferDto transferDto) {
        bankAccountService.transfer(customerId, transferDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{customerId}/{iban}/balance")
    public ResponseEntity checkBalance(@PathVariable Long customerId, @PathVariable String iban) {
        return ResponseEntity.ok(bankAccountService.checkBalance(customerId, iban));
    }

    @PostMapping(value = "/{customerId}/bankaccount/create")
    public ResponseEntity createAccount(@PathVariable Long customerId, @RequestBody CreateAccountDto createAccountDto) throws URISyntaxException {
        bankAccountService.createAccount(customerId, createAccountDto);
        return ResponseEntity.created(new URI(String.format("/api/%d/bankaccounts", customerId))).build();
    }

    @PostMapping(value = "/{customerId}/bankaccount/deactivate")
    public ResponseEntity deactivateAccount(@PathVariable Long customerId, @Valid @RequestBody IbanDto ibanDto) {
        bankAccountService.changeAccountStatus(customerId, ibanDto, Boolean.FALSE);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{customerId}/bankaccount/activate")
    public ResponseEntity activateAccount(@PathVariable Long customerId, @Valid @RequestBody IbanDto ibanDto) {
        bankAccountService.changeAccountStatus(customerId, ibanDto, Boolean.TRUE);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{iban}/transactions")
    public ResponseEntity transactions(@PathVariable String iban) {
        return ResponseEntity.ok(bankAccountService.getTransactionHistory(iban));
    }
}

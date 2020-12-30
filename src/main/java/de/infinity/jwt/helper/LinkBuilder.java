package de.infinity.jwt.helper;

import de.infinity.jwt.resource.BankAccountResource;
import de.infinity.jwt.dto.BankAccountDto;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.List;

public class LinkBuilder {

    public List<BankAccountDto> build(List<BankAccountDto> bankAccountDtoList, Long customerId) {
        bankAccountDtoList.stream().forEach(s -> build(s, customerId));
        return bankAccountDtoList;
    }

    public BankAccountDto build(BankAccountDto bankAccountDto, Long customerId) {
        Link self = WebMvcLinkBuilder.linkTo(BankAccountResource.class)
                .slash(String.format("%s/bankaccounts/%s", customerId, bankAccountDto.getId())).withSelfRel();
        Link transactions = WebMvcLinkBuilder.linkTo(BankAccountResource.class)
                .slash(String.format("%s/transactions", bankAccountDto.getIban())).withRel(("transactions"));
        bankAccountDto.add(self, transactions);
        return bankAccountDto;
    }

}

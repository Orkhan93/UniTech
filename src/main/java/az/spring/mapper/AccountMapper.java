package az.spring.mapper;

import az.spring.entity.Account;
import az.spring.request.AccountRequest;
import az.spring.response.AccountResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {

    Account fromRequestToModel(AccountRequest accountRequest);

    AccountResponse fromModelToResponse(Account account);

}
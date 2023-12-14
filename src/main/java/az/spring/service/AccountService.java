package az.spring.service;

import az.spring.entity.Account;
import az.spring.entity.User;
import az.spring.exception.AccountAlreadyExistsException;
import az.spring.exception.AccountNotFoundException;
import az.spring.exception.UserNotFoundException;
import az.spring.exception.error.ErrorMessage;
import az.spring.mapper.AccountMapper;
import az.spring.repository.AccountRepository;
import az.spring.repository.UserRepository;
import az.spring.request.AccountRequest;
import az.spring.response.AccountResponse;
import az.spring.response.AccountResponseList;
import az.spring.wrapper.AccountWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    public AccountResponse createAccount(Long userId, AccountRequest request) {
        log.info("Inside accountRequest {}", request);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(HttpStatus.NOT_FOUND.name(), ErrorMessage.USER_NOT_FOUND));
        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(request.getAccountNumber());
        if (optionalAccount.isPresent()) {
            throw new AccountAlreadyExistsException(HttpStatus.BAD_REQUEST.name(), ErrorMessage.ACCOUNT_ALREADY_EXISTS);
        }
        Account account = accountMapper.fromRequestToModel(request);
        account.setUser(user);
        account.setStatus(true);
        log.info("Inside createAccount {}", account);
        return accountMapper.fromModelToResponse(accountRepository.save(account));
    }

    public AccountResponse changeStatusAccount(Long userId, AccountRequest request) {
        log.info("Inside accountRequest {}", request);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(HttpStatus.NOT_FOUND.name(), ErrorMessage.USER_NOT_FOUND));
        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(request.getAccountNumber());
        if (optionalAccount.isEmpty()) {
            throw new AccountNotFoundException(HttpStatus.NOT_FOUND.name(), ErrorMessage.ACCOUNT_NOT_FOUND);
        }
        Account account = optionalAccount.get();
        account.setStatus(false);
        log.info("Inside changeStatusAccount {}", account);
        return accountMapper.fromModelToResponse(accountRepository.save(account));
    }

    public AccountResponseList allActiveAccounts(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(HttpStatus.NOT_FOUND.name(), ErrorMessage.USER_NOT_FOUND));
        List<AccountWrapper> accountResponses = accountRepository.allActiveAccountByUserId(user.getId());
        AccountResponseList list = new AccountResponseList();
        list.setAccountResponseList(accountResponses);
        log.info("Inside allActiveAccounts {}", list);
        return list;
    }

}
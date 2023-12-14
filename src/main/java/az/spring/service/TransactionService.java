package az.spring.service;

import az.spring.entity.Account;
import az.spring.exception.IllegalArgumentException;
import az.spring.exception.error.ErrorMessage;
import az.spring.repository.AccountRepository;
import az.spring.request.TransferRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;

    @Transactional
    public void moneyTransfer(TransferRequest transferRequest) {
        log.info("Inside moneyTransfer {}", transferRequest);
        Account fromAccount = accountRepository.findByAccountNumber(transferRequest.getFromAccountNumber()).orElseThrow(
                () -> new IllegalArgumentException(HttpStatus.NOT_FOUND.name(), ErrorMessage.PAYER_NOT_FOUND));
        Account toAccount = accountRepository.findByAccountNumber(transferRequest.getToAccountNumber()).orElseThrow(
                () -> new IllegalArgumentException(HttpStatus.NOT_FOUND.name(), ErrorMessage.RECEIVER_NOT_FOUND));
        if (!fromAccount.getStatus() || !toAccount.getStatus()) {
            throw new IllegalArgumentException(HttpStatus.BAD_REQUEST.name(), ErrorMessage.DISABLE_ACCOUNT);
        }
        if (fromAccount.getAccountNumber().equals(toAccount.getAccountNumber())) {
            throw new IllegalArgumentException(HttpStatus.CONFLICT.name(), ErrorMessage.SAME_ACCOUNT);
        }
        if (fromAccount.getBalance().compareTo(transferRequest.getAmount()) < 0) {
            throw new IllegalArgumentException(HttpStatus.BAD_REQUEST.name(), ErrorMessage.INSUFFICIENT_FUNDS);
        }
        fromAccount.setBalance(fromAccount.getBalance().subtract(transferRequest.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(transferRequest.getAmount()));
        log.info("Inside fromAccount {}", fromAccount);
        accountRepository.save(fromAccount);
        log.info("Inside toAccount {}", toAccount);
        accountRepository.save(toAccount);
    }

}
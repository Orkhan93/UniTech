package az.spring.controller;

import az.spring.request.AccountRequest;
import az.spring.response.AccountResponse;
import az.spring.response.AccountResponseList;
import az.spring.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/add/{userId}")
    public ResponseEntity<AccountResponse> createAccount(@PathVariable(name = "userId") Long userId,
                                                         @RequestBody AccountRequest accountRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(userId, accountRequest));
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<AccountResponseList> allActiveAccounts(@PathVariable(name = "userId") Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.allActiveAccounts(userId));
    }

    @PutMapping("/change-status/{userId}")
    public ResponseEntity<AccountResponse> changeStatusAccount(@PathVariable(name = "userId") Long userId,
                                                               @RequestBody AccountRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.changeStatusAccount(userId, request));
    }

}
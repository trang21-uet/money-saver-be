package com.trangnx.saver.controller;

import com.trangnx.saver.dto.AccountDTO;
import com.trangnx.saver.dto.ApiResponse;
import com.trangnx.saver.service.AccountService;
import com.trangnx.saver.util.AuthenticationHelper;
import com.trangnx.saver.util.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountDTO>>> getAllAccounts() {
        return ResponseWrapper.ok(accountService.getAllAccounts(AuthenticationHelper.getCurrentUserId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountDTO>> getAccountById(@PathVariable Long id) {
        return ResponseWrapper.ok(accountService.getAccountById(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AccountDTO>> createAccount(@RequestBody AccountDTO dto) {
        return ResponseWrapper.created(accountService.createAccount(AuthenticationHelper.getCurrentUserId(), dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountDTO>> updateAccount(@PathVariable Long id, @RequestBody AccountDTO dto) {
        return ResponseWrapper.ok(accountService.updateAccount(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseWrapper.ok("Account deleted");
    }
}

package com.trangnx.saver.controller;

import com.trangnx.saver.dto.AccountDTO;
import com.trangnx.saver.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "Accounts", description = "Account management endpoints (Cash, Bank, E-Wallet, Credit Card)")
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    @Operation(summary = "Get all accounts", description = "Get all accounts for a user, sorted by default first")
    public ResponseEntity<List<AccountDTO>> getAllAccounts(
            @RequestParam(defaultValue = "1") Long userId) {
        List<AccountDTO> accounts = accountService.getAllAccounts(userId);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get account by ID", description = "Get a specific account by its ID")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable Long id) {
        AccountDTO account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    @PostMapping
    @Operation(summary = "Create new account", description = "Create a new account (cash, bank, e-wallet, credit card)")
    public ResponseEntity<AccountDTO> createAccount(
            @RequestParam(defaultValue = "1") Long userId,
            @RequestBody AccountDTO accountDTO) {
        AccountDTO created = accountService.createAccount(userId, accountDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update account", description = "Update an existing account")
    public ResponseEntity<AccountDTO> updateAccount(
            @PathVariable Long id,
            @RequestBody AccountDTO accountDTO) {
        AccountDTO updated = accountService.updateAccount(id, accountDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete account", description = "Delete an account")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
package com.trangnx.saver.controller;

import com.trangnx.saver.dto.AccountDTO;
import com.trangnx.saver.dto.ApiResponse;
import com.trangnx.saver.service.AccountService;
import com.trangnx.saver.util.AuthenticationHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @Operation(
            summary = "Get all accounts",
            description = "Get all accounts for authenticated user, sorted by default first",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<List<AccountDTO>>> getAllAccounts() {
        Long userId = AuthenticationHelper.getCurrentUserId();
        List<AccountDTO> accounts = accountService.getAllAccounts(userId);
        return ResponseEntity.ok(ApiResponse.success(accounts));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get account by ID",
            description = "Get a specific account by its ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<AccountDTO>> getAccountById(@PathVariable Long id) {
        AccountDTO account = accountService.getAccountById(id);
        return ResponseEntity.ok(ApiResponse.success(account));
    }

    @PostMapping
    @Operation(
            summary = "Create new account",
            description = "Create a new account (cash, bank, e-wallet, credit card) for authenticated user",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<AccountDTO>> createAccount(@RequestBody AccountDTO accountDTO) {
        Long userId = AuthenticationHelper.getCurrentUserId();
        AccountDTO created = accountService.createAccount(userId, accountDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Account created successfully", created));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update account",
            description = "Update an existing account",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<AccountDTO>> updateAccount(
            @PathVariable Long id,
            @RequestBody AccountDTO accountDTO) {
        AccountDTO updated = accountService.updateAccount(id, accountDTO);
        return ResponseEntity.ok(ApiResponse.success("Account updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete account",
            description = "Delete an account",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<Void>> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok(ApiResponse.success("Account deleted successfully"));
    }
}
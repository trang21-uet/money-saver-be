package com.trangnx.saver.controller;

import com.trangnx.saver.dto.ApiResponse;
import com.trangnx.saver.dto.TransactionDTO;
import com.trangnx.saver.service.TransactionService;
import com.trangnx.saver.util.AuthenticationHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Transaction management endpoints (Income & Expense)")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    @Operation(
            summary = "Get all transactions",
            description = "Get all transactions for authenticated user",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getAllTransactions() {
        Long userId = AuthenticationHelper.getCurrentUserId();
        List<TransactionDTO> transactions = transactionService.getAllTransactions(userId);
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }

    @GetMapping("/range")
    @Operation(
            summary = "Get transactions by date range",
            description = "Get transactions within a date range for authenticated user",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Long userId = AuthenticationHelper.getCurrentUserId();
        List<TransactionDTO> transactions = transactionService.getTransactionsByDateRange(userId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get transaction by ID",
            description = "Get a specific transaction by its ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<TransactionDTO>> getTransactionById(@PathVariable Long id) {
        TransactionDTO transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(ApiResponse.success(transaction));
    }

    @PostMapping
    @Operation(
            summary = "Create transaction",
            description = "Create a new income or expense transaction for authenticated user",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<TransactionDTO>> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        Long userId = AuthenticationHelper.getCurrentUserId();
        TransactionDTO created = transactionService.createTransaction(userId, transactionDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Transaction created successfully", created));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete transaction",
            description = "Delete a transaction and revert account balance",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<Void>> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok(ApiResponse.success("Transaction deleted successfully"));
    }

    @GetMapping("/summary/income")
    @Operation(
            summary = "Get total income",
            description = "Get total income for a date range for authenticated user",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalIncome(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Long userId = AuthenticationHelper.getCurrentUserId();
        BigDecimal total = transactionService.getTotalIncome(userId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(total));
    }

    @GetMapping("/summary/expense")
    @Operation(
            summary = "Get total expense",
            description = "Get total expense for a date range for authenticated user",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalExpense(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Long userId = AuthenticationHelper.getCurrentUserId();
        BigDecimal total = transactionService.getTotalExpense(userId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(total));
    }
}
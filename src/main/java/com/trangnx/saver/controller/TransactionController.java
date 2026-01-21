package com.trangnx.saver.controller;

import com.trangnx.saver.dto.TransactionDTO;
import com.trangnx.saver.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Get all transactions", description = "Get all transactions for a user")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions(
            @RequestParam(defaultValue = "1") Long userId) {
        List<TransactionDTO> transactions = transactionService.getAllTransactions(userId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/range")
    @Operation(summary = "Get transactions by date range", description = "Get transactions within a date range")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByDateRange(
            @RequestParam(defaultValue = "1") Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByDateRange(userId, startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by ID", description = "Get a specific transaction by its ID")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long id) {
        TransactionDTO transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping
    @Operation(summary = "Create transaction", description = "Create a new income or expense transaction")
    public ResponseEntity<TransactionDTO> createTransaction(
            @RequestParam(defaultValue = "1") Long userId,
            @RequestBody TransactionDTO transactionDTO) {
        TransactionDTO created = transactionService.createTransaction(userId, transactionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete transaction", description = "Delete a transaction and revert account balance")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary/income")
    @Operation(summary = "Get total income", description = "Get total income for a date range")
    public ResponseEntity<BigDecimal> getTotalIncome(
            @RequestParam(defaultValue = "1") Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        BigDecimal total = transactionService.getTotalIncome(userId, startDate, endDate);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/summary/expense")
    @Operation(summary = "Get total expense", description = "Get total expense for a date range")
    public ResponseEntity<BigDecimal> getTotalExpense(
            @RequestParam(defaultValue = "1") Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        BigDecimal total = transactionService.getTotalExpense(userId, startDate, endDate);
        return ResponseEntity.ok(total);
    }
}
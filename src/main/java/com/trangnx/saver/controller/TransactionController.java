package com.trangnx.saver.controller;

import com.trangnx.saver.dto.ApiResponse;
import com.trangnx.saver.dto.TransactionDTO;
import com.trangnx.saver.service.TransactionService;
import com.trangnx.saver.util.AuthenticationHelper;
import com.trangnx.saver.util.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getAllTransactions() {
        return ResponseWrapper.ok(transactionService.getAllTransactions(AuthenticationHelper.getCurrentUserId()));
    }

    @GetMapping("/range")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseWrapper.ok(transactionService.getTransactionsByDateRange(AuthenticationHelper.getCurrentUserId(), start, end));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionDTO>> getTransactionById(@PathVariable Long id) {
        return ResponseWrapper.ok(transactionService.getTransactionById(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TransactionDTO>> createTransaction(@RequestBody TransactionDTO dto) {
        return ResponseWrapper.created(transactionService.createTransaction(AuthenticationHelper.getCurrentUserId(), dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseWrapper.ok("Transaction deleted");
    }

    @GetMapping("/income")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalIncome(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseWrapper.ok(transactionService.getTotalIncome(AuthenticationHelper.getCurrentUserId(), start, end));
    }

    @GetMapping("/expense")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalExpense(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseWrapper.ok(transactionService.getTotalExpense(AuthenticationHelper.getCurrentUserId(), start, end));
    }
}

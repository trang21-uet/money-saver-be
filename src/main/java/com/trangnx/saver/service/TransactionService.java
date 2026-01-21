package com.trangnx.saver.service;

import com.trangnx.saver.dto.TransactionDTO;
import com.trangnx.saver.entity.Account;
import com.trangnx.saver.entity.Category;
import com.trangnx.saver.entity.Transaction;
import com.trangnx.saver.entity.User;
import com.trangnx.saver.repository.AccountRepository;
import com.trangnx.saver.repository.CategoryRepository;
import com.trangnx.saver.repository.TransactionRepository;
import com.trangnx.saver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;

    public List<TransactionDTO> getAllTransactions(Long userId) {
        return transactionRepository.findByUserIdOrderByTransactionDateDesc(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TransactionDTO> getTransactionsByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByUserIdAndTransactionDateBetween(userId, startDate, endDate)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TransactionDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return convertToDTO(transaction);
    }

    public TransactionDTO createTransaction(Long userId, TransactionDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Transaction transaction = Transaction.builder()
                .user(user)
                .account(account)
                .category(category)
                .type(Transaction.TransactionType.valueOf(dto.getType()))
                .amount(dto.getAmount())
                .description(dto.getDescription())
                .transactionDate(dto.getTransactionDate())
                .notes(dto.getNotes())
                .tags(dto.getTags())
                .build();

        // Update account balance
        if (transaction.getType() == Transaction.TransactionType.INCOME) {
            account.setBalance(account.getBalance().add(dto.getAmount()));
        } else {
            account.setBalance(account.getBalance().subtract(dto.getAmount()));
        }
        accountRepository.save(account);

        Transaction saved = transactionRepository.save(transaction);
        return convertToDTO(saved);
    }

    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Revert account balance
        Account account = transaction.getAccount();
        if (transaction.getType() == Transaction.TransactionType.INCOME) {
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        } else {
            account.setBalance(account.getBalance().add(transaction.getAmount()));
        }
        accountRepository.save(account);

        transactionRepository.deleteById(id);
    }

    public BigDecimal getTotalIncome(Long userId, LocalDate startDate, LocalDate endDate) {
        BigDecimal total = transactionRepository.sumAmountByUserAndTypeAndDateRange(
                userId, Transaction.TransactionType.INCOME, startDate, endDate);
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal getTotalExpense(Long userId, LocalDate startDate, LocalDate endDate) {
        BigDecimal total = transactionRepository.sumAmountByUserAndTypeAndDateRange(
                userId, Transaction.TransactionType.EXPENSE, startDate, endDate);
        return total != null ? total : BigDecimal.ZERO;
    }

    private TransactionDTO convertToDTO(Transaction transaction) {
        return TransactionDTO.builder()
                .id(transaction.getId())
                .accountId(transaction.getAccount().getId())
                .accountName(transaction.getAccount().getName())
                .categoryId(transaction.getCategory().getId())
                .categoryName(transaction.getCategory().getName())
                .type(transaction.getType().name())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .transactionDate(transaction.getTransactionDate())
                .notes(transaction.getNotes())
                .tags(transaction.getTags())
                .build();
    }
}
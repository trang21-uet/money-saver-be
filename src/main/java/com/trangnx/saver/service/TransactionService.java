package com.trangnx.saver.service;

import com.trangnx.saver.dto.TransactionDTO;
import com.trangnx.saver.entity.Transaction;
import com.trangnx.saver.exception.ResourceNotFoundException;
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
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;

    public List<TransactionDTO> getAllTransactions(Long userId) {
        return transactionRepository.findByUserIdOrderByTransactionDateDesc(userId)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<TransactionDTO> getTransactionsByDateRange(Long userId, LocalDate start, LocalDate end) {
        return transactionRepository.findByUserIdAndTransactionDateBetween(userId, start, end)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public TransactionDTO getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", id));
    }

    @Transactional
    public TransactionDTO createTransaction(Long userId, TransactionDTO dto) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        var account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", dto.getAccountId()));
        var category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", dto.getCategoryId()));

        Transaction transaction = Transaction.builder()
                .user(user).account(account).category(category)
                .type(Transaction.TransactionType.valueOf(dto.getType()))
                .amount(dto.getAmount())
                .description(dto.getDescription())
                .transactionDate(dto.getTransactionDate() != null ? dto.getTransactionDate() : LocalDate.now())
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

        return convertToDTO(transactionRepository.save(transaction));
    }

    @Transactional
    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) throw new ResourceNotFoundException("Transaction", "id", id);
        transactionRepository.deleteById(id);
    }

    public BigDecimal getTotalIncome(Long userId, LocalDate start, LocalDate end) {
        return transactionRepository.sumAmountByUserAndTypeAndDateRange(userId, Transaction.TransactionType.INCOME, start, end);
    }

    public BigDecimal getTotalExpense(Long userId, LocalDate start, LocalDate end) {
        return transactionRepository.sumAmountByUserAndTypeAndDateRange(userId, Transaction.TransactionType.EXPENSE, start, end);
    }

    private TransactionDTO convertToDTO(Transaction t) {
        return TransactionDTO.builder()
                .id(t.getId())
                .accountId(t.getAccount().getId())
                .accountName(t.getAccount().getName())
                .categoryId(t.getCategory().getId())
                .categoryName(t.getCategory().getName())
                .type(t.getType().name())
                .amount(t.getAmount())
                .description(t.getDescription())
                .transactionDate(t.getTransactionDate())
                .notes(t.getNotes())
                .tags(t.getTags())
                .build();
    }
}

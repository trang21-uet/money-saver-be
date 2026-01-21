package com.trangnx.saver.service;

import com.trangnx.saver.dto.UserDTO;
import com.trangnx.saver.dto.UserStatsDTO;
import com.trangnx.saver.entity.Account;
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
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return convertToDTO(user);
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return convertToDTO(user);
    }

    public UserDTO updateUser(Long id, UserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Validation
        if (dto.getFullName() != null && dto.getFullName().trim().isEmpty()) {
            throw new RuntimeException("Full name cannot be empty");
        }

        // Update fields
        if (dto.getFullName() != null) {
            user.setFullName(dto.getFullName());
        }
        if (dto.getAvatarUrl() != null) {
            user.setAvatarUrl(dto.getAvatarUrl());
        }

        User updated = userRepository.save(user);
        return convertToDTO(updated);
    }

    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setIsActive(false);
        userRepository.save(user);
    }

    public void activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setIsActive(true);
        userRepository.save(user);
    }

    public UserStatsDTO getUserStats(Long userId) {
        // Verify user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Count resources
        Long accountsCount = accountRepository.countByUserId(userId);
        Long categoriesCount = categoryRepository.countByUserId(userId);
        Long transactionsCount = transactionRepository.countByUserId(userId);

        // Calculate total balance from all accounts
        BigDecimal totalBalance = accountRepository.findByUserId(userId)
                .stream()
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate monthly income/expense (current month)
        YearMonth currentMonth = YearMonth.now();
        LocalDate startDate = currentMonth.atDay(1);
        LocalDate endDate = currentMonth.atEndOfMonth();

        BigDecimal monthlyIncome = transactionRepository.sumAmountByUserAndTypeAndDateRange(
                userId, Transaction.TransactionType.INCOME, startDate, endDate);

        BigDecimal monthlyExpense = transactionRepository.sumAmountByUserAndTypeAndDateRange(
                userId, Transaction.TransactionType.EXPENSE, startDate, endDate);

        return UserStatsDTO.builder()
                .userId(userId)
                .accountsCount(accountsCount)
                .categoriesCount(categoriesCount)
                .transactionsCount(transactionsCount)
                .totalBalance(totalBalance != null ? totalBalance : BigDecimal.ZERO)
                .monthlyIncome(monthlyIncome != null ? monthlyIncome : BigDecimal.ZERO)
                .monthlyExpense(monthlyExpense != null ? monthlyExpense : BigDecimal.ZERO)
                .build();
    }

    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .avatarUrl(user.getAvatarUrl())
                .provider(user.getProvider().name())
                .isActive(user.getIsActive())
                .build();
    }
}
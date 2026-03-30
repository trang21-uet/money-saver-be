package com.trangnx.saver.service;

import com.trangnx.saver.dto.UserDTO;
import com.trangnx.saver.dto.UserStatsDTO;
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

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    public UserDTO getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO dto) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        if (dto.getFullName() != null) user.setFullName(dto.getFullName());
        if (dto.getAvatarUrl() != null) user.setAvatarUrl(dto.getAvatarUrl());
        return convertToDTO(userRepository.save(user));
    }

    @Transactional
    public void deactivateUser(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setIsActive(false);
        userRepository.save(user);
    }

    @Transactional
    public void activateUser(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setIsActive(true);
        userRepository.save(user);
    }

    public UserStatsDTO getUserStats(Long userId) {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        BigDecimal totalBalance = accountRepository.findByUserId(userId).stream()
                .map(a -> a.getBalance() != null ? a.getBalance() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal monthlyIncome = transactionRepository.sumAmountByUserAndTypeAndDateRange(
                userId, Transaction.TransactionType.INCOME, startOfMonth, endOfMonth);
        BigDecimal monthlyExpense = transactionRepository.sumAmountByUserAndTypeAndDateRange(
                userId, Transaction.TransactionType.EXPENSE, startOfMonth, endOfMonth);

        return UserStatsDTO.builder()
                .userId(userId)
                .accountsCount(accountRepository.countByUserId(userId))
                .categoriesCount(categoryRepository.countByUserId(userId))
                .transactionsCount(transactionRepository.countByUserId(userId))
                .totalBalance(totalBalance)
                .monthlyIncome(monthlyIncome != null ? monthlyIncome : BigDecimal.ZERO)
                .monthlyExpense(monthlyExpense != null ? monthlyExpense : BigDecimal.ZERO)
                .build();
    }

    private UserDTO convertToDTO(com.trangnx.saver.entity.User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .avatarUrl(user.getAvatarUrl())
                .provider(user.getProvider() != null ? user.getProvider().name() : null)
                .isActive(user.getIsActive())
                .build();
    }
}

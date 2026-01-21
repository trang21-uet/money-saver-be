package com.trangnx.saver.service;

import com.trangnx.saver.dto.AccountDTO;
import com.trangnx.saver.entity.Account;
import com.trangnx.saver.entity.User;
import com.trangnx.saver.repository.AccountRepository;
import com.trangnx.saver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public List<AccountDTO> getAllAccounts(Long userId) {
        return accountRepository.findByUserIdOrderByIsDefaultDesc(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AccountDTO getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return convertToDTO(account);
    }

    public AccountDTO createAccount(Long userId, AccountDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (accountRepository.existsByUserIdAndName(userId, dto.getName())) {
            throw new RuntimeException("Account name already exists");
        }

        Account account = Account.builder()
                .user(user)
                .name(dto.getName())
                .type(Account.AccountType.valueOf(dto.getType()))
                .balance(dto.getBalance())
                .currency(dto.getCurrency() != null ? dto.getCurrency() : "VND")
                .icon(dto.getIcon())
                .color(dto.getColor())
                .isDefault(dto.getIsDefault() != null ? dto.getIsDefault() : false)
                .build();

        Account saved = accountRepository.save(account);
        return convertToDTO(saved);
    }

    public AccountDTO updateAccount(Long id, AccountDTO dto) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (dto.getName() != null) account.setName(dto.getName());
        if (dto.getBalance() != null) account.setBalance(dto.getBalance());
        if (dto.getIcon() != null) account.setIcon(dto.getIcon());
        if (dto.getColor() != null) account.setColor(dto.getColor());

        Account updated = accountRepository.save(account);
        return convertToDTO(updated);
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    private AccountDTO convertToDTO(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .name(account.getName())
                .type(account.getType().name())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .icon(account.getIcon())
                .color(account.getColor())
                .isDefault(account.getIsDefault())
                .build();
    }
}
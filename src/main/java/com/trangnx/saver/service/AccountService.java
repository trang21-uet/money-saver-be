package com.trangnx.saver.service;

import com.trangnx.saver.dto.AccountDTO;
import com.trangnx.saver.entity.Account;
import com.trangnx.saver.exception.ResourceNotFoundException;
import com.trangnx.saver.repository.AccountRepository;
import com.trangnx.saver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public List<AccountDTO> getAllAccounts(Long userId) {
        return accountRepository.findByUserIdOrderByIsDefaultDesc(userId)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public AccountDTO getAccountById(Long id) {
        return accountRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", id));
    }

    @Transactional
    public AccountDTO createAccount(Long userId, AccountDTO dto) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Account account = Account.builder()
                .user(user)
                .name(dto.getName())
                .type(Account.AccountType.valueOf(dto.getType()))
                .balance(dto.getBalance())
                .currency(dto.getCurrency())
                .icon(dto.getIcon())
                .color(dto.getColor())
                .isDefault(dto.getIsDefault() != null && dto.getIsDefault())
                .build();
        return convertToDTO(accountRepository.save(account));
    }

    @Transactional
    public AccountDTO updateAccount(Long id, AccountDTO dto) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", id));
        if (dto.getName() != null) account.setName(dto.getName());
        if (dto.getBalance() != null) account.setBalance(dto.getBalance());
        if (dto.getCurrency() != null) account.setCurrency(dto.getCurrency());
        if (dto.getIcon() != null) account.setIcon(dto.getIcon());
        if (dto.getColor() != null) account.setColor(dto.getColor());
        if (dto.getIsDefault() != null) account.setIsDefault(dto.getIsDefault());
        return convertToDTO(accountRepository.save(account));
    }

    @Transactional
    public void deleteAccount(Long id) {
        if (!accountRepository.existsById(id)) throw new ResourceNotFoundException("Account", "id", id);
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

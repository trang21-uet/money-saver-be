package com.trangnx.saver.repository;

import com.trangnx.saver.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByUserId(Long userId);

    List<Account> findByUserIdOrderByIsDefaultDesc(Long userId);

    Optional<Account> findByUserIdAndIsDefaultTrue(Long userId);

    boolean existsByUserIdAndName(Long userId, String name);

    Long countByUserId(Long userId);
}
package com.trangnx.saver.repository;

import com.trangnx.saver.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserIdOrderByTransactionDateDesc(Long userId);
    List<Transaction> findByUserIdAndTransactionDateBetween(Long userId, LocalDate start, LocalDate end);
    List<Transaction> findByUserIdAndCategoryId(Long userId, Long categoryId);
    List<Transaction> findByUserIdAndAccountId(Long userId, Long accountId);
    Long countByUserId(Long userId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.id = :userId AND t.type = :type AND t.transactionDate BETWEEN :start AND :end")
    BigDecimal sumAmountByUserAndTypeAndDateRange(@Param("userId") Long userId,
                                                  @Param("type") Transaction.TransactionType type,
                                                  @Param("start") LocalDate start,
                                                  @Param("end") LocalDate end);
}

package com.trangnx.saver.repository;

import com.trangnx.saver.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUserId(Long userId);

    List<Category> findByUserIdAndType(Long userId, Category.TransactionType type);

    boolean existsByUserIdAndName(Long userId, String name);

    Long countByUserId(Long userId);
}
package com.trangnx.saver.service;

import com.trangnx.saver.dto.CategoryDTO;
import com.trangnx.saver.entity.Category;
import com.trangnx.saver.entity.User;
import com.trangnx.saver.repository.CategoryRepository;
import com.trangnx.saver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public List<CategoryDTO> getAllCategories(Long userId) {
        return categoryRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return convertToDTO(category);
    }

    public CategoryDTO createCategory(Long userId, CategoryDTO dto) {
        // Get user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Check duplicate name
        if (categoryRepository.existsByUserIdAndName(userId, dto.getName())) {
            throw new RuntimeException("Category name already exists");
        }

        Category category = Category.builder()
                .user(user)
                .name(dto.getName())
                .type(Category.TransactionType.valueOf(dto.getType()))
                .icon(dto.getIcon())
                .color(dto.getColor())
                .isDefault(false)
                .build();

        Category saved = categoryRepository.save(category);
        return convertToDTO(saved);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    private CategoryDTO convertToDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .type(category.getType().name())
                .icon(category.getIcon())
                .color(category.getColor())
                .isDefault(category.getIsDefault())
                .build();
    }
}
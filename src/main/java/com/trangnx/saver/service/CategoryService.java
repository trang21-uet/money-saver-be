package com.trangnx.saver.service;

import com.trangnx.saver.dto.CategoryDTO;
import com.trangnx.saver.entity.Category;
import com.trangnx.saver.exception.ResourceNotFoundException;
import com.trangnx.saver.repository.CategoryRepository;
import com.trangnx.saver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public List<CategoryDTO> getAllCategories(Long userId) {
        return categoryRepository.findByUserId(userId)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }

    @Transactional
    public CategoryDTO createCategory(Long userId, CategoryDTO dto) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Category category = Category.builder()
                .user(user)
                .name(dto.getName())
                .type(Category.TransactionType.valueOf(dto.getType()))
                .icon(dto.getIcon())
                .color(dto.getColor())
                .isDefault(false)
                .build();
        return convertToDTO(categoryRepository.save(category));
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) throw new ResourceNotFoundException("Category", "id", id);
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

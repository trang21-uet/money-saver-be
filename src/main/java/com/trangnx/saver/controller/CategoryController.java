package com.trangnx.saver.controller;

import com.trangnx.saver.dto.ApiResponse;
import com.trangnx.saver.dto.CategoryDTO;
import com.trangnx.saver.service.CategoryService;
import com.trangnx.saver.util.AuthenticationHelper;
import com.trangnx.saver.util.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getAllCategories() {
        return ResponseWrapper.ok(categoryService.getAllCategories(AuthenticationHelper.getCurrentUserId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDTO>> getCategoryById(@PathVariable Long id) {
        return ResponseWrapper.ok(categoryService.getCategoryById(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryDTO>> createCategory(@RequestBody CategoryDTO dto) {
        return ResponseWrapper.created(categoryService.createCategory(AuthenticationHelper.getCurrentUserId(), dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseWrapper.ok("Category deleted");
    }
}

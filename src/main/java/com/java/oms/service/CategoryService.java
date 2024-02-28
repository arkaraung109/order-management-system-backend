package com.java.oms.service;

import com.java.oms.dto.CategoryDto;
import com.java.oms.dto.PaginationResponse;

import java.util.List;

public interface CategoryService {

    boolean findExistByName(String name);

    CategoryDto findById(Long id);

    List<CategoryDto> findAll();

    PaginationResponse<CategoryDto> findPage(String keyword, int pageNo, int pageSize);

    void save(CategoryDto categoryDto);

    void update(CategoryDto categoryDto);

    void deleteById(Long id);

}

package com.java.oms.service;

import com.java.oms.dto.CategoryDto;
import com.java.oms.dto.PaginationResponse;
import com.java.oms.entity.Category;
import com.java.oms.exception.NotFoundException;
import com.java.oms.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public boolean findExistByName(String name) {
        return this.categoryRepository.existsByName(name);
    }

    @Override
    public CategoryDto findById(Long id) {
        Optional<Category> optional = this.categoryRepository.findById(id);
        return optional.map((category) -> this.modelMapper.map(category, CategoryDto.class)).orElseThrow(() -> new NotFoundException("Category is not found."));
    }

    @Override
    public PaginationResponse<CategoryDto> findPage(String keyword, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("id"));
        Page<Category> page = this.categoryRepository.findPage(keyword, pageable);
        PaginationResponse<CategoryDto> paginationResponse = new PaginationResponse<CategoryDto>();
        paginationResponse.setElementList(page.stream().map((category) -> this.modelMapper.map(category, CategoryDto.class)).collect(Collectors.toList()));
        paginationResponse.setTotalElements(page.getTotalElements());
        paginationResponse.setTotalPages(page.getTotalPages());
        paginationResponse.setPageSize(page.getSize());
        return paginationResponse;
    }

    @Override
    public void save(CategoryDto categoryDto) {
        this.categoryRepository.save(this.modelMapper.map(categoryDto, Category.class));
    }

    @Override
    public void update(CategoryDto categoryDto) {
        this.categoryRepository.save(this.modelMapper.map(categoryDto, Category.class));
    }

    @Override
    public void delete(CategoryDto categoryDto) {
        this.categoryRepository.delete(this.modelMapper.map(categoryDto, Category.class));
    }

}

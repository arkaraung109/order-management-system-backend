package com.java.oms.service;

import com.java.oms.dto.PaginationResponse;
import com.java.oms.dto.ProductDto;

public interface ProductService {

    boolean findExistByName(String name);

    ProductDto findById(Long id);

    PaginationResponse<ProductDto> findPage(String categoryName, String keyword, int pageNo, int pageSize);

    ProductDto save(ProductDto productDto);

    void update(ProductDto productDto);

    void deleteById(Long id);

}
package com.java.oms.service;

import com.java.oms.converter.ManufacturingCostConverter;
import com.java.oms.converter.RetailPriceConverter;
import com.java.oms.dto.PaginationResponse;
import com.java.oms.dto.ProductDto;
import com.java.oms.entity.Product;
import com.java.oms.exception.NotFoundException;
import com.java.oms.repository.ManufacturingCostRepository;
import com.java.oms.repository.ProductRepository;
import com.java.oms.repository.RetailPriceRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ManufacturingCostRepository manufacturingCostRepository;

    @Autowired
    private RetailPriceRepository retailPriceRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        TypeMap<Product, ProductDto> propertyMapper = this.modelMapper.createTypeMap(Product.class, ProductDto.class);
        propertyMapper.addMappings(mapper -> {
            mapper.using(new ManufacturingCostConverter()).map(Product::getManufacturingCostList, ProductDto::setManufacturingCost);
            mapper.using(new RetailPriceConverter()).map(Product::getRetailPriceList, ProductDto::setRetailPrice);
        });
    }

    @Override
    public boolean findExistByName(String name) {
        return this.productRepository.existsByName(name);
    }

    @Override
    public ProductDto findById(Long id) {
        Optional<Product> optional = this.productRepository.findById(id);
        return optional.map((product) -> this.modelMapper.map(product, ProductDto.class)).orElseThrow(() -> new NotFoundException("Product is not found."));
    }

    @Override
    public PaginationResponse<ProductDto> findPage(String categoryName, String keyword, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("id"));
        Page<Product> page = this.productRepository.findPage(categoryName, keyword, pageable);
        PaginationResponse<ProductDto> paginationResponse = new PaginationResponse<ProductDto>();
        paginationResponse.setElementList(page.stream().map((product) -> this.modelMapper.map(product, ProductDto.class)).collect(Collectors.toList()));
        paginationResponse.setTotalElements(page.getTotalElements());
        paginationResponse.setTotalPages(page.getTotalPages());
        paginationResponse.setPageSize(page.getSize());
        return paginationResponse;
    }

    @Override
    public ProductDto save(ProductDto productDto) {
        Product product = this.productRepository.save(this.modelMapper.map(productDto, Product.class));
        productDto.setId(product.getId());
        return productDto;
    }

    @Override
    public void update(ProductDto productDto) {
        this.productRepository.save(this.modelMapper.map(productDto, Product.class));
    }

    @Override
    public void deleteById(Long id) {
        this.productRepository.deleteById(id);
    }

}

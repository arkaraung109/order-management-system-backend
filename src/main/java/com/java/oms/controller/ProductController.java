package com.java.oms.controller;

import com.java.oms.dto.*;
import com.java.oms.exception.DuplicatedException;
import com.java.oms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping(value = "/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ManufacturingCostService manufacturingCostService;

    @Autowired
    private RetailPriceService retailPriceService;

    @Autowired
    private OrderDetailsService orderDetailsService;

    @Autowired
    private DeliveryRouteDetailsService deliveryRouteDetailsService;

    @GetMapping("/findById")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public ResponseEntity<ProductDto> findById(@RequestParam("id") Long id) {
        // Find product by id
        return ResponseEntity.status(HttpStatus.OK).body(this.productService.findById(id));
    }

    @GetMapping("/findPage")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'DELIVERY_MANAGER')")
    public ResponseEntity<PaginationResponse<ProductDto>> findPage(
            @RequestParam(defaultValue = "") String categoryName,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(this.productService.findPage(categoryName, keyword, pageNo, pageSize));
    }

    @PostMapping("/create")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public ResponseEntity<HttpResponse> create(@RequestBody ProductDto requestDto) {
        // If name already exists
        if(this.productService.findExistByName(requestDto.getName())) {
            throw new DuplicatedException("Duplicated Name");
        }

        // Product is saved
        ProductDto productDto = this.productService.save(requestDto);

        // Manufacturing Cost is saved for this product
        ManufacturingCostDto manufacturingCostDto = ManufacturingCostDto.builder()
                .cost(productDto.getManufacturingCost())
                .product(productDto)
                .build();
        this.manufacturingCostService.save(manufacturingCostDto);

        // Retail Price is saved for this product
        RetailPriceDto retailPriceDto = RetailPriceDto.builder()
                .price(productDto.getRetailPrice())
                .product(productDto)
                .build();
        this.retailPriceService.save(retailPriceDto);

        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.CREATED.value())
                .title("Product Creation")
                .message("Successfully created.")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public ResponseEntity<HttpResponse> update(@RequestBody ProductDto requestDto) {
        // Find product by id
        ProductDto productDto = this.productService.findById(requestDto.getId());

        // If name already exists
        if(this.productService.findExistByName(requestDto.getName()) && !Objects.equals(requestDto.getName(), productDto.getName())) {
            throw new DuplicatedException("Duplicated Name");
        }

        // Product is updated
        productDto.setName(requestDto.getName());
        productDto.setCategory(requestDto.getCategory());
        this.productService.update(productDto);

        // If manufacturing cost is changed
        if(!Objects.equals(requestDto.getManufacturingCost(), productDto.getManufacturingCost())) {
            // Manufacturing cost is saved for this product
            ManufacturingCostDto manufacturingCostDto = ManufacturingCostDto.builder()
                    .cost(requestDto.getManufacturingCost())
                    .product(productDto)
                    .build();
            this.manufacturingCostService.save(manufacturingCostDto);
        }

        // If retail price is changed
        if(!Objects.equals(requestDto.getRetailPrice(), productDto.getRetailPrice())) {
            // Retail price is saved for this product
            RetailPriceDto retailPriceDto = RetailPriceDto.builder()
                    .price(requestDto.getRetailPrice())
                    .product(productDto)
                    .build();
            this.retailPriceService.save(retailPriceDto);
        }

        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.OK.value())
                .title("Product Update")
                .message("Successfully updated.")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public ResponseEntity<HttpResponse> delete(@PathVariable("id") Long id) {
        // Find product by id
        ProductDto productDto = this.productService.findById(id);

        // If this product exists in order_details table or delivery_route_details table
        boolean findExistInOrderDetails = this.orderDetailsService.findExistByProductId(id);
        boolean findExistInDeliveryDetails = this.deliveryRouteDetailsService.findExistByProductId(id);
        if(findExistInOrderDetails || findExistInDeliveryDetails) {
            throw new DataIntegrityViolationException("This product is used in order details or delivery route details.");
        }

        // Product is deleted
        this.productService.deleteById(id);

        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.OK.value())
                .title("Product Deletion")
                .message("Successfully deleted.")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}

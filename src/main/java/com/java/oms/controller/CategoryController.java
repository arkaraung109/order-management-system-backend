package com.java.oms.controller;

import com.java.oms.dto.CategoryDto;
import com.java.oms.dto.HttpResponse;
import com.java.oms.dto.PaginationResponse;
import com.java.oms.dto.RoleDto;
import com.java.oms.exception.DuplicatedException;
import com.java.oms.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/findById")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public ResponseEntity<CategoryDto> findById(@RequestParam("id") Long id) {
        // Find category by id
        return ResponseEntity.status(HttpStatus.OK).body(this.categoryService.findById(id));
    }

    @GetMapping("/findAll")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<CategoryDto>> findAll() {
        // Find all categories
        return ResponseEntity.status(HttpStatus.OK).body(this.categoryService.findAll());
    }

    @GetMapping("/findPage")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'DELIVERY_MANAGER')")
    public ResponseEntity<PaginationResponse<CategoryDto>> findPage(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(this.categoryService.findPage(keyword, pageNo, pageSize));
    }

    @PostMapping("/create")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public ResponseEntity<HttpResponse> create(@RequestBody CategoryDto requestDto) {
        // If name already exists
        if(this.categoryService.findExistByName(requestDto.getName())) {
            throw new DuplicatedException("Duplicated Name");
        }

        // Category is saved
        this.categoryService.save(requestDto);

        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.CREATED.value())
                .title("Category Creation")
                .message("Successfully created.")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public ResponseEntity<HttpResponse> update(@RequestBody CategoryDto requestDto) {
        // Find category by id
        CategoryDto categoryDto = this.categoryService.findById(requestDto.getId());

        // If name already exists
        if(this.categoryService.findExistByName(requestDto.getName()) && !Objects.equals(requestDto.getName(), categoryDto.getName())) {
            throw new DuplicatedException("Duplicated Name");
        }

        // Category is updated
        categoryDto.setName(requestDto.getName());
        this.categoryService.update(categoryDto);

        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.OK.value())
                .title("Category Update")
                .message("Successfully updated.")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public ResponseEntity<HttpResponse> delete(@PathVariable("id") Long id) {
        // Find category by id
        CategoryDto categoryDto = this.categoryService.findById(id);

        // Category is deleted
        this.categoryService.deleteById(id);

        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.OK.value())
                .title("Category Deletion")
                .message("Successfully deleted.")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}

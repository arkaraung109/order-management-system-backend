package com.java.oms.controller;

import com.java.oms.dto.HttpResponse;
import com.java.oms.dto.ManufacturingCostDto;
import com.java.oms.dto.PaginationResponse;
import com.java.oms.dto.RetailPriceDto;
import com.java.oms.service.RetailPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/retailPrice")
public class RetailPriceController {

    @Autowired
    private RetailPriceService retailPriceService;

    @GetMapping("/findPage")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'DELIVERY_MANAGER')")
    public ResponseEntity<PaginationResponse<RetailPriceDto>> findPage(
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(this.retailPriceService.findPage(productId, pageNo, pageSize));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public ResponseEntity<HttpResponse> delete(@PathVariable("id") Long id) {
        // Find retail price by id
        RetailPriceDto retailPriceDto = this.retailPriceService.findById(id);

        // Retail price is deleted
        this.retailPriceService.deleteById(id);

        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.OK.value())
                .title("Retail Price Deletion")
                .message("Successfully deleted.")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}

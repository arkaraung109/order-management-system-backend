package com.java.oms.controller;

import com.java.oms.dto.HttpResponse;
import com.java.oms.dto.ManufacturingCostDto;
import com.java.oms.dto.PaginationResponse;
import com.java.oms.service.ManufacturingCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/manufacturingCost")
public class ManufacturingCostController {

    @Autowired
    private ManufacturingCostService manufacturingCostService;

    @GetMapping("/findPage")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'DELIVERY_MANAGER')")
    public ResponseEntity<PaginationResponse<ManufacturingCostDto>> findPage(
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(this.manufacturingCostService.findPage(productId, pageNo, pageSize));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public ResponseEntity<HttpResponse> delete(@PathVariable("id") Long id) {
        // Find manufacturing cost by id
        ManufacturingCostDto manufacturingCostDto = this.manufacturingCostService.findById(id);

        // Manufacturing cost is deleted
        this.manufacturingCostService.deleteById(id);

        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.OK.value())
                .title("Manufacturing Cost Deletion")
                .message("Successfully deleted.")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}

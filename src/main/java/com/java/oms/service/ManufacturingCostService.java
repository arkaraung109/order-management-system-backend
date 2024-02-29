package com.java.oms.service;

import com.java.oms.dto.ManufacturingCostDto;
import com.java.oms.dto.PaginationResponse;

public interface ManufacturingCostService {

    ManufacturingCostDto findById(Long id);

    PaginationResponse<ManufacturingCostDto> findPage(Long productId, int pageNo, int pageSize);

    void save(ManufacturingCostDto manufacturingCostDto);

    void deleteById(Long id);

}

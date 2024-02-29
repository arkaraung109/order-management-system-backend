package com.java.oms.service;

import com.java.oms.dto.PaginationResponse;
import com.java.oms.dto.RetailPriceDto;

public interface RetailPriceService {

    RetailPriceDto findById(Long id);

    PaginationResponse<RetailPriceDto> findPage(Long productId, int pageNo, int pageSize);

    void save(RetailPriceDto retailPriceDto);

    void deleteById(Long id);

}

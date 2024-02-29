package com.java.oms.service;

import com.java.oms.dto.PaginationResponse;
import com.java.oms.dto.RetailPriceDto;
import com.java.oms.entity.RetailPrice;
import com.java.oms.exception.NotFoundException;
import com.java.oms.repository.RetailPriceRepository;
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
public class RetailPriceServiceImpl implements RetailPriceService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RetailPriceRepository retailPriceRepository;

    @Override
    public RetailPriceDto findById(Long id) {
        Optional<RetailPrice> optional = this.retailPriceRepository.findById(id);
        return optional.map((retailPrice) -> this.modelMapper.map(retailPrice, RetailPriceDto.class)).orElseThrow(() -> new NotFoundException("Retail price is not found."));
    }

    @Override
    public PaginationResponse<RetailPriceDto> findPage(Long productId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("id"));
        Page<RetailPrice> page = this.retailPriceRepository.findPage(productId, pageable);
        PaginationResponse<RetailPriceDto> paginationResponse = new PaginationResponse<RetailPriceDto>();
        paginationResponse.setElementList(page.stream().map((retailPrice) -> this.modelMapper.map(retailPrice, RetailPriceDto.class)).collect(Collectors.toList()));
        paginationResponse.setTotalElements(page.getTotalElements());
        paginationResponse.setTotalPages(page.getTotalPages());
        paginationResponse.setPageSize(page.getSize());
        return paginationResponse;
    }

    @Override
    public void save(RetailPriceDto retailPriceDto) {
        this.retailPriceRepository.save(this.modelMapper.map(retailPriceDto, RetailPrice.class));
    }

    @Override
    public void deleteById(Long id) {
        this.retailPriceRepository.deleteById(id);
    }

}

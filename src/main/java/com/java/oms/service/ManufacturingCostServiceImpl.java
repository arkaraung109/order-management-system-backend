package com.java.oms.service;

import com.java.oms.dto.ManufacturingCostDto;
import com.java.oms.dto.PaginationResponse;
import com.java.oms.entity.ManufacturingCost;
import com.java.oms.exception.NotFoundException;
import com.java.oms.repository.ManufacturingCostRepository;
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
public class ManufacturingCostServiceImpl implements ManufacturingCostService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ManufacturingCostRepository manufacturingCostRepository;

    @Override
    public ManufacturingCostDto findById(Long id) {
        Optional<ManufacturingCost> optional = this.manufacturingCostRepository.findById(id);
        return optional.map((manufacturingCost) -> this.modelMapper.map(manufacturingCost, ManufacturingCostDto.class)).orElseThrow(() -> new NotFoundException("Manufacturing cost is not found."));
    }

    @Override
    public PaginationResponse<ManufacturingCostDto> findPage(Long productId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("id"));
        Page<ManufacturingCost> page = this.manufacturingCostRepository.findPage(productId, pageable);
        PaginationResponse<ManufacturingCostDto> paginationResponse = new PaginationResponse<ManufacturingCostDto>();
        paginationResponse.setElementList(page.stream().map((manufacturingCost) -> this.modelMapper.map(manufacturingCost, ManufacturingCostDto.class)).collect(Collectors.toList()));
        paginationResponse.setTotalElements(page.getTotalElements());
        paginationResponse.setTotalPages(page.getTotalPages());
        paginationResponse.setPageSize(page.getSize());
        return paginationResponse;
    }

    @Override
    public void save(ManufacturingCostDto manufacturingCostDto) {
        this.manufacturingCostRepository.save(this.modelMapper.map(manufacturingCostDto, ManufacturingCost.class));
    }

    @Override
    public void deleteById(Long id) {
        this.manufacturingCostRepository.deleteById(id);
    }

}

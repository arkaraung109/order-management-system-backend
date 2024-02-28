package com.java.oms.service;

import com.java.oms.dto.ManufacturingCostDto;
import com.java.oms.entity.ManufacturingCost;
import com.java.oms.repository.ManufacturingCostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManufacturingCostServiceImpl implements ManufacturingCostService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ManufacturingCostRepository manufacturingCostRepository;

    @Override
    public void save(ManufacturingCostDto manufacturingCostDto) {
        this.manufacturingCostRepository.save(this.modelMapper.map(manufacturingCostDto, ManufacturingCost.class));
    }

}

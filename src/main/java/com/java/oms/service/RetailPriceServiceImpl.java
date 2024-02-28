package com.java.oms.service;

import com.java.oms.dto.RetailPriceDto;
import com.java.oms.entity.RetailPrice;
import com.java.oms.repository.RetailPriceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RetailPriceServiceImpl implements RetailPriceService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RetailPriceRepository retailPriceRepository;


    @Override
    public void save(RetailPriceDto retailPriceDto) {
        this.retailPriceRepository.save(this.modelMapper.map(retailPriceDto, RetailPrice.class));
    }

}

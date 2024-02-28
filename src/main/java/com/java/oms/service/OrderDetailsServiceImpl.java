package com.java.oms.service;

import com.java.oms.repository.OrderDetailsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Override
    public boolean findExistByProductId(Long id) {
        return this.orderDetailsRepository.existsByProductId(id);
    }

}

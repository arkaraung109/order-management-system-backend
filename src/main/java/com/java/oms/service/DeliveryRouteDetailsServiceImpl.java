package com.java.oms.service;

import com.java.oms.repository.DeliveryRouteDetailsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryRouteDetailsServiceImpl implements DeliveryRouteDetailsService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DeliveryRouteDetailsRepository deliveryRouteDetailsRepository;

    @Override
    public boolean findExistByProductId(Long id) {
        return this.deliveryRouteDetailsRepository.existsByProductId(id);
    }

}

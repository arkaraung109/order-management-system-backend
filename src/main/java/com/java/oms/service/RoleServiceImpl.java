package com.java.oms.service;

import com.java.oms.dto.RoleDto;
import com.java.oms.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<RoleDto> findAll() {
        return this.roleRepository.findAll().stream().map((role) -> this.modelMapper.map(role, RoleDto.class)).collect(Collectors.toList());
    }

}

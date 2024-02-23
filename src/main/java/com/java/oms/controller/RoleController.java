package com.java.oms.controller;

import com.java.oms.dto.RoleDto;
import com.java.oms.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/findAll")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<RoleDto>> findAll() {
        // Find all roles
        return ResponseEntity.status(HttpStatus.OK).body(this.roleService.findAll());
    }

}

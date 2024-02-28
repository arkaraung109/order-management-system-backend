package com.java.oms.repository;

import com.java.oms.entity.ManufacturingCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManufacturingCostRepository extends JpaRepository<ManufacturingCost, Long> {
}

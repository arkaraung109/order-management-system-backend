package com.java.oms.repository;

import com.java.oms.entity.ManufacturingCost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ManufacturingCostRepository extends JpaRepository<ManufacturingCost, Long> {

    @Query("select m from ManufacturingCost m where (m.product.id=:productId)")
    Page<ManufacturingCost> findPage(Long productId, Pageable pageable);

}

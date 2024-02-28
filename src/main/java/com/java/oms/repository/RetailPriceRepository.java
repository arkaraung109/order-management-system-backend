package com.java.oms.repository;

import com.java.oms.entity.RetailPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RetailPriceRepository extends JpaRepository<RetailPrice, Long> {
}

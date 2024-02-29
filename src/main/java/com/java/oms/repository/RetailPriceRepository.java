package com.java.oms.repository;

import com.java.oms.entity.RetailPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RetailPriceRepository extends JpaRepository<RetailPrice, Long> {

    @Query("select r from RetailPrice r where (r.product.id=:productId)")
    Page<RetailPrice> findPage(Long productId, Pageable pageable);

}

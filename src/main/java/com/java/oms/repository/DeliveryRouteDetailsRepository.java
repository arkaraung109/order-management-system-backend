package com.java.oms.repository;

import com.java.oms.entity.DeliveryRouteDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRouteDetailsRepository extends JpaRepository<DeliveryRouteDetails, Long> {

    boolean existsByProductId(Long id);

}

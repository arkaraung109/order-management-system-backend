package com.java.oms.repository;

import com.java.oms.entity.DeliveryRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRouteRepository extends JpaRepository<DeliveryRoute, Long> {
}

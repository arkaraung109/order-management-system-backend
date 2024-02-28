package com.java.oms.repository;

import com.java.oms.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName(String name);

    @Query("select p from Product p where (p.name like :keyword%) and (cast(p.category.name as string) like :categoryName%)")
    Page<Product> findPage(@Param("categoryName") String categoryName, @Param("keyword") String keyword, Pageable pageable);

}

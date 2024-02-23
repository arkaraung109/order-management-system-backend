package com.java.oms.repository;

import com.java.oms.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);

    @Query("select c from Category c where (c.name like :keyword%)")
    Page<Category> findPage(String keyword, Pageable pageable);

}

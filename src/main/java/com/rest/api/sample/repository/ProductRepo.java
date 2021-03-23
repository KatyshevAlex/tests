package com.rest.api.sample.repository;

import com.rest.api.sample.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {

    @Query(value = "SELECT * FROM products p " +
            "WHERE category = ?1 " +
            "ORDER BY " +
            "availability DESC, " +
            "discounted_price ASC, " +
            "id ASC ",
            nativeQuery = true)
    List<Product> findSorted(String category);

    @Query(value = "SELECT * FROM products p " +
            "WHERE category = ?1 and " +
            "availability = ?2 ",
            nativeQuery = true)
    List<Product> findTwoParams(String category, Boolean availability);

    @Query(value = "SELECT * FROM products p " +
            " ORDER BY ID ASC ",
            nativeQuery = true)
    List<Product> findAllSortedById();
}

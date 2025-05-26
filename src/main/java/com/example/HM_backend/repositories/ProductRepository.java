package com.example.HM_backend.repositories;

import com.example.HM_backend.models.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByDeletedFalse();
    Optional<Product> findByIdAndDeletedFalse(Long id);
}

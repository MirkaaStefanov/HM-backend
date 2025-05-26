package com.example.HM_backend.repositories;

import com.example.HM_backend.models.entity.Product;
import com.example.HM_backend.models.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

}

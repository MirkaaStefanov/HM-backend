package com.example.HM_backend.services.impl;


import com.example.HM_backend.models.dto.FilterCriteriaDTO;
import com.example.HM_backend.models.dto.ProductDTO;
import com.example.HM_backend.models.dto.ProductSpecification;
import com.example.HM_backend.models.entity.Product;
import com.example.HM_backend.models.entity.ProductImage;
import com.example.HM_backend.repositories.ProductImageRepository;
import com.example.HM_backend.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ModelMapper modelMapper;

    public ProductDTO save(ProductDTO productDTO) throws IOException {
        Product product = modelMapper.map(productDTO, Product.class);
        Product savedProduct = productRepository.save(product);
        List<ProductImage> images = new ArrayList<>();
        for(String image : productDTO.getImageStrings()){
            byte[] decodedImage = Base64.getDecoder().decode(image);
            ProductImage productImage = new ProductImage();
            productImage.setProduct(savedProduct);
            productImage.setImageData(decodedImage);
            images.add(productImage);
            productImageRepository.save(productImage);
        }
        savedProduct.setImages(images);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    public List<ProductDTO> findAll() {
        List<Product> products = productRepository.findAllByDeletedFalse();
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    public ProductDTO findById(Long id) throws ChangeSetPersister.NotFoundException {
        Product firstProduct = productRepository.findByIdAndDeletedFalse(id).orElseThrow(ChangeSetPersister.NotFoundException::new);
        return modelMapper.map(firstProduct, ProductDTO.class);
    }

    public ProductDTO update(Long id, ProductDTO firstProductDTO) {
        Product firstProduct = modelMapper.map(firstProductDTO, Product.class);
        firstProduct.setId(id);
        return modelMapper.map(productRepository.save(firstProduct), ProductDTO.class);
    }

    public void delete(Long id) throws ChangeSetPersister.NotFoundException {
        Product firstProduct = productRepository.findByIdAndDeletedFalse(id).orElseThrow(ChangeSetPersister.NotFoundException::new);
        firstProduct.setDeleted(true);
        productRepository.save(firstProduct);
    }

    public List<ProductDTO> filterProducts(FilterCriteriaDTO criteria) {

        Specification<Product> spec = ProductSpecification.isNotDeleted();

        if (criteria.getGlobalSearchText() != null && !criteria.getGlobalSearchText().trim().isEmpty()) {
            spec = spec.and(ProductSpecification.textContains(criteria.getGlobalSearchText()));
        }

        if (criteria.getMinPrice() != null || criteria.getMaxPrice() != null) {
            spec = spec.and(ProductSpecification.priceBetween(criteria.getMinPrice(), criteria.getMaxPrice()));
        }
        if (criteria.getMinArea() != null) {
            spec = spec.and(ProductSpecification.hasMinArea(criteria.getMinArea()));
        }
        if (criteria.getMinRooms() != null) {
            spec = spec.and(ProductSpecification.hasMinRooms(criteria.getMinRooms()));
        }
        if (criteria.getCity() != null && !criteria.getCity().trim().isEmpty()) {
            spec = spec.and(ProductSpecification.hasCity(criteria.getCity()));
        }
        if (criteria.getRegionId() != null) {
            spec = spec.and(ProductSpecification.hasRegion(criteria.getRegionId()));
        }
        if (criteria.getState() != null) {
            spec = spec.and(ProductSpecification.hasState(criteria.getState()));
        }

        List<Product> products = productRepository.findAll(spec); // Use findAll with the Specification

        return products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }
}

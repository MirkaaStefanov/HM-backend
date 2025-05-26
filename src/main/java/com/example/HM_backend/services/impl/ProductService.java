package com.example.HM_backend.services.impl;


import com.example.HM_backend.models.dto.ProductDTO;
import com.example.HM_backend.models.entity.Product;
import com.example.HM_backend.models.entity.ProductImage;
import com.example.HM_backend.repositories.ProductImageRepository;
import com.example.HM_backend.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.crossstore.ChangeSetPersister;
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

}

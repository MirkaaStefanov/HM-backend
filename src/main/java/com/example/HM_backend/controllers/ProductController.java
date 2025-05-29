package com.example.HM_backend.controllers;

import com.example.HM_backend.enums.State;
import com.example.HM_backend.models.dto.FilterCriteriaDTO;
import com.example.HM_backend.models.dto.ProductDTO;
import com.example.HM_backend.services.impl.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/save")
    public ResponseEntity<ProductDTO> save(@RequestBody ProductDTO productDTO, @RequestHeader("Authorization") String auth) throws IOException {
        return ResponseEntity.ok(productService.save(productDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id, @RequestHeader("Authorization") String auth) throws ChangeSetPersister.NotFoundException {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO firstProductDTO, @RequestHeader("Authorization") String auth) throws ChangeSetPersister.NotFoundException {
        return ResponseEntity.ok(productService.update(id, firstProductDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, @RequestHeader("Authorization") String auth) throws ChangeSetPersister.NotFoundException {
        productService.delete(id);
        return ResponseEntity.ok("Product have been deleted successfully");
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ProductDTO>> filterProducts(
            @RequestParam(required = false) String globalSearchText,// Will be parsed to ProductType
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minArea,
            @RequestParam(required = false) Integer maxArea,
            @RequestParam(required = false) Integer minRooms,
            @RequestParam(required = false) Integer maxRooms,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) String state // Will be parsed to State
    ) {
        FilterCriteriaDTO criteria = new FilterCriteriaDTO();
        criteria.setGlobalSearchText(globalSearchText);
        criteria.setMinPrice(minPrice);
        criteria.setMaxPrice(maxPrice);
        criteria.setMinArea(minArea);
        criteria.setMaxArea(maxArea);
        criteria.setMinRooms(minRooms);
        criteria.setMaxRooms(maxRooms);
        criteria.setCity(city);
        criteria.setRegionId(regionId);
        if (state != null && !state.isEmpty()) {
            try {
                criteria.setState(State.valueOf(state.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid State: " + state);
            }
        }
        return ResponseEntity.ok(productService.filterProducts(criteria));
    }

}

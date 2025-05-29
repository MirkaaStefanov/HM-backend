package com.example.HM_backend.models.dto;

import com.example.HM_backend.enums.State;
import com.example.HM_backend.models.entity.Product;
import com.example.HM_backend.models.entity.Region;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
    public static Specification<Product> isNotDeleted() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isFalse(root.get("deleted"));
    }

    public static Specification<Product> hasMinArea(Integer minArea) {
        return (root, query, criteriaBuilder) ->
                minArea == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("area"), minArea);
    }

    public static Specification<Product> hasMinRooms(Integer minRooms) {
        return (root, query, criteriaBuilder) ->
                minRooms == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("rooms"), minRooms);
    }

    public static Specification<Product> priceBetween(Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>(); // Use the correct Predicate type

            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
            if (predicates.isEmpty()) {
                return null;
            }

            // No cast needed here, criteriaBuilder.and() takes an array of Predicate and returns a Predicate
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }


    public static Specification<Product> hasRegion(Long regionId) {
        return (root, query, criteriaBuilder) -> {
            if (regionId == null) {
                return null;
            }
            Join<Product, Region> regionJoin = root.join("region"); // Join with the Region entity
            return criteriaBuilder.equal(regionJoin.get("id"), regionId);
        };
    }

    public static Specification<Product> hasCity(String city) {
        return (root, query, criteriaBuilder) ->
                city == null || city.trim().isEmpty() ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("city")), "%" + city.toLowerCase() + "%");
    }

    public static Specification<Product> hasState(State state) {
        return (root, query, criteriaBuilder) ->
                state == null ? null : criteriaBuilder.equal(root.get("state"), state);
    }

    // Generic text search across multiple fields (name, description)
    public static Specification<Product> textContains(String text) {
        return (root, query, criteriaBuilder) -> {
            if (text == null || text.trim().isEmpty()) {
                return null;
            }
            String lowerCaseText = "%" + text.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), lowerCaseText),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), lowerCaseText),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("city")), lowerCaseText),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("state").as(String.class)), lowerCaseText) // Search enum display text
            );
        };
    }
}

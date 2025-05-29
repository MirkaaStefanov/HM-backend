package com.example.HM_backend.repositories;

import com.example.HM_backend.models.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    List<Region> findAllByDeletedFalse();
    Optional<Region> findByIdAndDeletedFalse(Long id);
}

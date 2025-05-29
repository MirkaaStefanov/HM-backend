package com.example.HM_backend.services.impl;

import com.example.HM_backend.models.dto.RegionDTO;
import com.example.HM_backend.models.entity.Region;
import com.example.HM_backend.repositories.RegionRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.openqa.selenium.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;
    private final ModelMapper modelMapper;

    public RegionDTO save(RegionDTO regionDTO) {
        Region region = modelMapper.map(regionDTO, Region.class);
        region.setDeleted(false);
        Region savedRegion = regionRepository.save(region);
        return modelMapper.map(savedRegion, RegionDTO.class);
    }

    public List<RegionDTO> findAll() {
        List<Region> regions = regionRepository.findAllByDeletedFalse();
        return regions.stream()
                .map(region -> modelMapper.map(region, RegionDTO.class))
                .collect(Collectors.toList());
    }

    public RegionDTO findById(Long id) throws NotFoundException {
        Region region = regionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(NotFoundException::new);
        return modelMapper.map(region, RegionDTO.class);
    }

    public RegionDTO update(Long id, RegionDTO regionDTO) throws NotFoundException {
        Region existingRegion = regionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(NotFoundException::new);
        Region regionToUpdate = modelMapper.map(regionDTO, Region.class);
        regionToUpdate.setId(id);
        regionToUpdate.setDeleted(existingRegion.isDeleted());
        Region updatedRegion = regionRepository.save(regionToUpdate);
        return modelMapper.map(updatedRegion, RegionDTO.class);
    }

    public void delete(Long id) throws NotFoundException {
        Region region = regionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(NotFoundException::new);
        region.setDeleted(true);
        regionRepository.save(region);
    }


}

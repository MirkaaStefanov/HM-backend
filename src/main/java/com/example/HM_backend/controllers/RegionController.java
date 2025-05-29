package com.example.HM_backend.controllers;

import com.example.HM_backend.models.dto.RegionDTO;
import com.example.HM_backend.services.impl.RegionService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/region")
public class RegionController {

    private final RegionService regionService;

    @PostMapping("/save")
    public ResponseEntity<RegionDTO> save(@RequestBody RegionDTO regionDTO, @RequestHeader(value = "Authorization", required = false) String auth) {
        return ResponseEntity.ok(regionService.save(regionDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<List<RegionDTO>> findAll( @RequestHeader(value = "Authorization", required = false) String auth) {
        return ResponseEntity.ok(regionService.findAll());
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<RegionDTO> findById(@PathVariable Long id, @RequestHeader(value = "Authorization", required = false) String auth) throws ChangeSetPersister.NotFoundException {
        return ResponseEntity.ok(regionService.findById(id));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<RegionDTO> update(@PathVariable Long id, @RequestBody RegionDTO regionDTO, @RequestHeader(value = "Authorization", required = false) String auth) throws ChangeSetPersister.NotFoundException {
        return ResponseEntity.ok(regionService.update(id, regionDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, @RequestHeader(value = "Authorization", required = false) String auth) throws ChangeSetPersister.NotFoundException {
        regionService.delete(id);
        return ResponseEntity.ok("Region has been deleted successfully.");
    }
}

package com.example.HM_backend.models.dto;

import com.example.HM_backend.enums.State;
import com.example.HM_backend.models.entity.Region;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long id;
    private List<String> imageStrings;
    private String name;
    private String description;
    private double price;
    private int area;
    private State state;
    private String city;
    private RegionDTO region;
    private int rooms;
    @JsonIgnore
    private List<MultipartFile> imageFiles;
    private boolean deleted;
}

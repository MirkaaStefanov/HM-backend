package com.example.HM_backend.models.dto;


import com.example.HM_backend.enums.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterCriteriaDTO {

    private String globalSearchText;
    private Double minPrice;
    private Double maxPrice;
    private Integer minArea;
    private Integer maxArea; // Optional, but good to have if you implement in HTML
    private Integer minRooms;
    private Integer maxRooms; // Optional
    private String city;
    private Long regionId;
    private State state;
}

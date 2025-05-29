package com.example.HM_backend.models.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionDTO {

    private Long id;
    private String name;
    boolean deleted;
}

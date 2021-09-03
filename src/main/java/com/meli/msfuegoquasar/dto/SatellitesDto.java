package com.meli.msfuegoquasar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SatellitesDto {

    private String name;
    private Double distance;
    private List<String> message;
}

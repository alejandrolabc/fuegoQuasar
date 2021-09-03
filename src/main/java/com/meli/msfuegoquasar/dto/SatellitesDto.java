package com.meli.msfuegoquasar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SatellitesDto {

    private String name;
    private Double distance;
    private List<String> message;
}

package com.meli.msfuegoquasar.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;


@Data
@ApiModel(value = "RequestDto")
public class RequestDto {
    private List<SatellitesDto> satellites;
}

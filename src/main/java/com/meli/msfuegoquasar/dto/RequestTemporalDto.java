package com.meli.msfuegoquasar.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "RequestCookieDto")
public class RequestTemporalDto {
    private Double distance;
    private List<String> message;
}

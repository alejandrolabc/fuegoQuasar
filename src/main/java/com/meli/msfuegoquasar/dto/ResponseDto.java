package com.meli.msfuegoquasar.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseDto {
    private LocationDto position;
    private String message;
}

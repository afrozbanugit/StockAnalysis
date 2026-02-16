package com.synergisticit.stock_analysis_service.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseDto {

    private String msg;
    private String symbol;
}

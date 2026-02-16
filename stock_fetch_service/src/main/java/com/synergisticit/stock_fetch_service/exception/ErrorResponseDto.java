package com.synergisticit.stock_fetch_service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseDto {

    String msg;
    String symbol;

}

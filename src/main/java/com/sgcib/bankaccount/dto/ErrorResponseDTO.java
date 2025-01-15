package com.sgcib.bankaccount.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponseDTO {
    private String message;
    private String code;
    private int status;
    private LocalDateTime timestamp;
}

package com.aziz.digitalwallet.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ApiError {

    private LocalDateTime timestamp;
    private String error;
    private String message;
    private List<String> details;

}

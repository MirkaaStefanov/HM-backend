package com.example.HM_backend.utils;


import com.example.HM_backend.exceptions.common.ApiException;
import com.example.HM_backend.models.dto.response.ExceptionResponse;

import java.time.LocalDateTime;

/**
 * ApiExceptionParser is a utility class responsible for parsing ApiException objects into ExceptionResponse objects.
 * It provides a static method to perform this parsing operation.
 */
public class ApiExceptionParser {
    public static ExceptionResponse parseException(ApiException exception) {
        return ExceptionResponse
                .builder()
                .dateTime(LocalDateTime.now())
                .message(exception.getMessage())
                .status(exception.getStatus())
                .statusCode(exception.getStatusCode())
                .build();
    }
}

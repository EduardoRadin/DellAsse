package com.dellasse.backend.exceptions;

import java.time.OffsetDateTime;
import java.util.Map;

/**
 * Contrato padrão de erro retornado pela API.
 */
public record ApiError(
    OffsetDateTime timestamp,
    int status,
    String error,
    String code,
    String message,
    String path,
    Map<String, String> fieldErrors
) {}


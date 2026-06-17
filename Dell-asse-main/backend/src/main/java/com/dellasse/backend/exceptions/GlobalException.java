package com.dellasse.backend.exceptions;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

/**
 * Manipulador global de exceções para a aplicação.
 * <p>
 * Esta classe captura exceções lançadas pelos controladores REST e
 * retorna respostas HTTP apropriadas com mensagens de erro detalhadas.
 *
 * @author  Dell'Asse
 * @version 1.0
 * @since 2025-11-21
 */
@RestControllerAdvice
public class GlobalException {
    
    /** 
     * Manipula exceções de validação de argumentos de método.
     *
     * @param ex A exceção lançada durante a validação.
     * @return ResponseEntity contendo os detalhes dos erros de validação.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {

        Map<String, String> fieldErrors = new LinkedHashMap<>();
       
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            if (error instanceof FieldError fieldError) {
                fieldErrors.put(fieldError.getField(), error.getDefaultMessage());
            } else {
                fieldErrors.put(error.getObjectName(), error.getDefaultMessage());
            }
        });

        ApiError body = new ApiError(
            OffsetDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            "VALIDATION_ERROR",
            "Falha de validação nos dados enviados.",
            getPath(request),
            fieldErrors
        );
        return ResponseEntity.badRequest().body(body);
    }

    /** 
     * Manipula exceções genéricas.
     *
     * @param ex A exceção genérica lançada.
     * @return ResponseEntity contendo uma mensagem de erro genérica.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex, WebRequest request) {
        ApiError body = new ApiError(
            OffsetDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
            "INTERNAL_SERVER_ERROR",
            "Erro interno do servidor.",
            getPath(request),
            null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    /** 
     * Manipula exceções de domínio personalizadas.
     *
     * @param ex A exceção de domínio lançada.
     * @return ResponseEntity contendo a mensagem de erro e o status apropriado.
     */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiError> handleDomainException(DomainException ex, WebRequest request) {
        HttpStatus status = HttpStatus.resolve(ex.getStatus());
        if (status == null) status = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiError body = new ApiError(
            OffsetDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            ex.getError().name(),
            ex.getMessage(),
            getPath(request),
            null
        );
        return ResponseEntity.status(status).body(body);
    }

    private static String getPath(WebRequest request) {
        if (request instanceof ServletWebRequest servlet) {
            return servlet.getRequest().getRequestURI();
        }
        return null;
    }
}

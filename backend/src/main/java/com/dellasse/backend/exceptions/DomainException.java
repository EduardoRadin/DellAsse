package com.dellasse.backend.exceptions;

/**
 * Exceção personalizada para erros de domínio na aplicação.
 * <p>
 * Esta exceção encapsula um {@link DomainError} que fornece detalhes
 * sobre o erro ocorrido, incluindo uma mensagem descritiva e um código de status.
 *
 * @author  Dell'Asse
 * @version 1.0
 * @since 2025-11-21
 */
public class DomainException extends RuntimeException {

    private final DomainError error;

    public DomainException(DomainError error) {
        super(error.getMessage());
        this.error = error;
    }

    public int getStatus() {
        return error.getStatus();
    }

    public DomainError getError() {
        return error;
    }
}

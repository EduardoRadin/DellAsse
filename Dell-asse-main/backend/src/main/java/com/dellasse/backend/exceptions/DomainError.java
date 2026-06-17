package com.dellasse.backend.exceptions;

/**
 * Enumeração que define os erros de domínio utilizados em regras de negócio.
 * <p>
 * Cada constante representa um erro específico da aplicação, contendo
 * uma mensagem descritiva e um código de status HTTP associado.
 *
 * <p>Usado principalmente em exceções de domínio para padronizar mensagens
 * e códigos de retorno.</p>
 *
 * @author  Dell'Asse
 * @version 1.0
 * @since   2025-11-21
 */
public enum DomainError {

    USER_NOT_FOUND("User not found", 404),
    USER_INVALID_PASSWORD("Invalid password", 401),
    USER_FORBIDDEN("The user does not have permission", 403),
    USER_NOT_LINKED("The user is not linked", 403),
    USER_ALREADY_EXISTS("User already exists", 409),
    USER_NOT_ADMIN("User is not admin", 403),
    USER_CANNOT_UPDATE_DATA("The user does not have permission to change the data.", 403),
    USER_NOT_FOUND_ENTERPRISE("User not found in enterprise", 404),
    USER_EMAIL_ALREADY_EXISTS("Email already exists", 409),
    USER_REQUIRE_USERNAME("Username is requerid", 400),
    USER_NOT_AUTHENTICATED("User not authenticated", 401),
    USER_NOT_FOUND_INTERNAL("User not found", 500),

    ENTERPRISE_NOT_FOUND("Enterprise not found", 404),
    ENTERPRISE_EXISTS("Enterprise already exists", 409),
    ENTERPRISE_FORBIDDEN("You do not have permission to access this enterprise", 403),
    ENTERPRISE_EXPIRED("Enterprise expired", 403),
    ENTERPRISE_NOT_FOUND_INTERNAL("Enterprise not found", 500),

    PRODUCT_NOT_FOUND("Product not found", 404),
    PRODUCT_ALREADY_EXISTS("Product already exists", 409),
    PRODUCT_INVALID_NAME("Product name is invalid", 400),
    PRODUCT_INVALID_PRICE("Product price is invalid", 400),
    PRODUCT_INVALID_QUANTITY("Product quantity is invalid", 400),
    PRODUCT_NOT_FOUND_INTERNAL("Product not found", 500),
    PRODUCT_OR_ENTERPRISE_NOT_FOUND_INTERNAL("Product or enterprise not found", 500),

    PARTY_INVALID("Party is invalid", 500),
    PARTY_NOT_FOUND("Party not found", 404),

    GALLERY_NOT_FOUND("Gallery not found", 404);

    private final String message;
    private final int status;

    /**
     * Cria uma nova instância de erro de domínio.
     *
     * @param message Mensagem descritiva do erro.
     * @param status  Código de status HTTP associado ao erro.
     */
    DomainError(String message, int status) {
        this.message = message;
        this.status = status;
    }

    /**
     * Retorna a mensagem descritiva do erro.
     *
     * @return Mensagem do erro.
     */
    public String getMessage() { return message; }

    /**
     * Retorna o código de status HTTP associado ao erro.
     *
     * @return Código HTTP do erro.
     */
    public int getStatus() { return status; }
}

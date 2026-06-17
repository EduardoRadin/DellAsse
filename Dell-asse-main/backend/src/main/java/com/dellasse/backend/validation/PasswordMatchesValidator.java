package com.dellasse.backend.validation;

import com.dellasse.backend.contracts.user.UserCreateRequest;
import com.dellasse.backend.contracts.user.UserUpdateRequest;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador para a anotação PasswordMatches.
 */
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object>{
    
    /**
     * Valida se as senhas coincidem.
     * @param value   Objeto a ser validado.
     * @param context Contexto da validação.
     * @return true se as senhas coincidirem, false caso contrário.
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
     
        if (value instanceof UserCreateRequest req) {
            return req.password().equals(req.confirmPassword());
        }
     
        if (value instanceof UserUpdateRequest request){
            if (request.password() == null || request.confirmPassword() == null) {
                return true;
            }

            return request.password() != null && request.confirmPassword() != null && request.password().equals(request.confirmPassword());
        }

        return false;
    }
}

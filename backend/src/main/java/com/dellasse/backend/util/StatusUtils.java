package com.dellasse.backend.util;

/**
 * Utilitário para status padrão do sistema.
 */
public enum StatusUtils {
    PENDING("PENDING"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    COMPLETED("COMPLETED");

    private final String value;

    /**
     * Construtor do enum StatusUtils.
     *
     * @param value Valor do status.
     */
    StatusUtils(String value) {
        this.value = value;
    }

    /**
     * Retorna o valor do status.
     *
     * @return Valor do status.
     */
    public String getValue() {
        return value;
    }
}
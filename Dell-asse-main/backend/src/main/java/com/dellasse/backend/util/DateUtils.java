package com.dellasse.backend.util;

import java.time.LocalDateTime;

/**
 * Utilitário para operações relacionadas a datas.
 */
public class DateUtils {
    private DateUtils() {}

    /**
     * Retorna a data e hora atual.
     *
     * @return Data e hora atual.
     */
    public static LocalDateTime now(){
        return LocalDateTime.now();
    }

}

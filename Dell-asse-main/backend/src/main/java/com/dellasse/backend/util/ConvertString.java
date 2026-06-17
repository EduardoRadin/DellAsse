package com.dellasse.backend.util;

import java.util.UUID;

/**
 * Utilitário para conversão de strings.
 */
public class ConvertString {
    
    /**
     * Converte uma string para UUID.
     *
     * @param uuid String representando o UUID.
     * @return UUID convertido.
     */
    public static UUID toUUID(String uuid) {
        return UUID.fromString(uuid);
    }

}

package com.dellasse.backend.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuração global de CORS para a aplicação.
 * <p>
 * Esta configuração permite que a aplicação aceite requisições de origens específicas,
 * definindo os métodos, cabeçalhos e credenciais permitidos.
 *
 * @author  Dell'Asse
 * @version 1.0
 * @since 2025-11-21
 */
@Configuration
public class CorsGlobalConfig {

    /** 
     * Configura a fonte de configuração CORS.
     *
     * @return CorsConfigurationSource com as definições de CORS.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(List.of(
            "http://localhost:*",
            "http://127.0.0.1:*"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}

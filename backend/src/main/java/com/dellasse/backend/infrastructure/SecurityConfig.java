package com.dellasse.backend.infrastructure;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;

/**
 * Configuração de segurança para a aplicação.
 * <p>
 * Esta configuração define os filtros de segurança, políticas de sessão,
 * e configurações de autenticação JWT para proteger os endpoints da API.
 *
 * @author  Dell'Asse
 * @version 1.0
 * @since 2025-11-21
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;


    @Value("${jwt.private.key}")
    private RSAPrivateKey privateKey;

    /**
     * Configura a cadeia de filtros de segurança.
     * @param http
     * @param userEnterpriseCheckFilter
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserEnterpriseCheckFilter userEnterpriseCheckFilter) throws Exception {
        var auth = UsernamePasswordAuthenticationFilter.class;
        http
                .addFilterBefore(new JwtCookieAuthenticationFilter(), auth)
                .addFilterAfter(userEnterpriseCheckFilter, AnonymousAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/user/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user/create").permitAll()
                        .requestMatchers(HttpMethod.GET, "/party/gallery").permitAll()
                        .requestMatchers(HttpMethod.GET, "/product/all").permitAll() //
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable()
                    //    .ignoringRequestMatchers(
                    //            request -> request.getMethod().equals("POST") && request.getServletPath().equals("/login"),
                    //            request -> request.getMethod().equals("POST") && request.getServletPath().equals("/user/create")
                    //    )
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    /** 
     * Configura o decodificador JWT.
     *
     * @return JwtDecoder para decodificar tokens JWT.
     */
    @Bean
    public JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    /** 
     * Configura o codificador JWT.
     *
     * @return JwtEncoder para codificar tokens JWT.
     */
    @Bean
    public JwtEncoder jwtEncoder(){
        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(privateKey).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    /** 
     * Configura o codificador de senhas BCrypt.
     *
     * @return BCryptPasswordEncoder para codificação de senhas.
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /** 
     * Configura o conversor de autenticação JWT.
     *
     * @return JwtAuthenticationConverter para converter tokens JWT em autenticações.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}

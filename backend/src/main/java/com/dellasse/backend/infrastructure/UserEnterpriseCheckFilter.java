package com.dellasse.backend.infrastructure;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dellasse.backend.exceptions.DomainError;
import com.dellasse.backend.exceptions.DomainException;
import com.dellasse.backend.repositories.EnterpriseRepository;
import com.dellasse.backend.service.UserService;
import com.dellasse.backend.util.ConvertString;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro para verificar a validade da empresa associada ao usuário autenticado.
 * <p>
 * Este filtro intercepta as requisições HTTP para garantir que o usuário autenticado
 * esteja associado a uma empresa válida e não expirada.
 *
 * @author  Dell'Asse
 * @version 1.0
 * @since 2025-11-21
 */
@Component
public class UserEnterpriseCheckFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;
    
    @Autowired
    private EnterpriseRepository enterpriseRepository;
    
    /** 
     * Processa a requisição HTTP para verificar a validade da empresa do usuário.
     *
     * @param request     A requisição HTTP.
     * @param response    A resposta HTTP.
     * @param filterChain O filtro da cadeia de filtros.
     * @throws ServletException Em caso de erro no servlet.
     * @throws IOException      Em caso de erro de I/O.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        doInternalFilter(request);
        filterChain.doFilter(request, response);
    }

    /** 
     * Define as condições para não aplicar este filtro.
     *
     * @param request A requisição HTTP.
     * @return boolean indicando se o filtro deve ser ignorado.
     * @throws ServletException Em caso de erro no servlet.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        String method = request.getMethod();
        if ("POST".equalsIgnoreCase(method)){
            if ("/user/login".equals(path) || "/user/create".equals(path)) return true;
        }
        return false;
    }

    /** 
     * Lógica principal do filtro para verificar a validade da empresa do usuário.
     *
     * @param request A requisição HTTP.
     */
    private void doInternalFilter(HttpServletRequest request){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Se não tiver auth, ou não estiver autenticado, ou for o usuário "anonymousUser" (Visitante)
        // Nós simplesmente retornamos e deixamos o Spring Security decidir se ele pode acessar a rota ou não.
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())){
            return; 
        }

        try {
            // Só tenta converter para UUID se tiver certeza que não é "anonymousUser"
            UUID userId = ConvertString.toUUID(auth.getName());
            UUID enterpriseId = userService.validateUserEnterprise(userId);

            if (enterpriseId != null){
                var enterprise = enterpriseRepository.findById(enterpriseId)
                    .orElseThrow(() -> new DomainException(DomainError.ENTERPRISE_NOT_FOUND));

                var expiration = enterprise.getDateExpiration();
                if (expiration != null && expiration.isBefore(LocalDateTime.now())){
                    throw new DomainException(DomainError.ENTERPRISE_EXPIRED);
                }
            }
        } catch (IllegalArgumentException e) {
            // Proteção extra: Se o token estiver malformado, ignora neste filtro 
            // e deixa o Spring Security barrar depois se necessário.
            return;
        }
    }
}

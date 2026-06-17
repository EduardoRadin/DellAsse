package com.dellasse.backend.service;


import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mapstruct.ap.shaded.freemarker.template.utility.NullArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.dellasse.backend.contracts.user.UserResponse;

import com.dellasse.backend.contracts.user.UserCreateRequest;
import com.dellasse.backend.contracts.user.UserLoginRequest;
import com.dellasse.backend.contracts.user.UserUpdateRequest;
import com.dellasse.backend.exceptions.DomainError;
import com.dellasse.backend.exceptions.DomainException;
import com.dellasse.backend.mappers.UserMapper;
import com.dellasse.backend.models.Enterprise;
import com.dellasse.backend.models.Role;
import com.dellasse.backend.models.User;
import com.dellasse.backend.repositories.RoleRepository;
import com.dellasse.backend.repositories.UserRepository;
import com.dellasse.backend.util.ConvertString;

import jakarta.persistence.EntityManager;

/**
 * Serviço para a entidade User.
 * <p>
 * Fornece métodos para operações relacionadas aos usuários.
 */
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private EntityManager entityManager;

    /**
     * Cria um novo usuário.
     *
     * @param request Dados do usuário a ser criado.
     * @param token   Token do usuário que está criando o novo usuário (pode ser nulo).
     * @return Resposta HTTP indicando o resultado da operação.
     */
    public ResponseEntity<?> createUser(UserCreateRequest request, String token){

        if (userRepository.existsByUsername(request.username())) {
            throw new DomainException(DomainError.USER_ALREADY_EXISTS);
        }

        if (userRepository.existsByEmail(request.email())){
            throw new DomainException(DomainError.USER_ALREADY_EXISTS);
        }
        User user = UserMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        defaultValues(user);

        if(token == null){
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        UUID userId = ConvertString.toUUID(token);
        UUID enterpriseId = validateUserEnterprise(userId);
        
        user.setEnterprise(entityManager.find(Enterprise.class, enterpriseId));
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    /**
     * Realiza o login de um usuário.
     *
     * @param loginRequest Dados de login do usuário.
     * @return Resposta HTTP contendo o token JWT e os papéis do usuário.
     */
    public ResponseEntity<?> loginUser(UserLoginRequest loginRequest){
        boolean userExists = userRepository.existsByUsername(loginRequest.username());
        if (!userExists){
            throw new DomainException(DomainError.USER_NOT_FOUND);
        }

        var user = userRepository.findByUsername(loginRequest.username())
            .orElseThrow(() -> new DomainException(DomainError.USER_NOT_FOUND));
        
        if (!user.isLoginCorret(loginRequest, passwordEncoder)){
            throw new DomainException(DomainError.USER_INVALID_PASSWORD);
        }

        var enterprise = user.getEnterprise();
        if (enterprise != null){
            var expiration = enterprise.getDateExpiration();
            if (expiration != null && expiration.isBefore(LocalDateTime.now())){
                throw new DomainException(DomainError.ENTERPRISE_EXPIRED);
            }
        }


        var now = Instant.now();
        var expiresIn = 30000L;  // em segundos (8h20min)

        var scope = user.getRoles()
            .stream()
            .map(Role::getName)
            .collect(Collectors.joining(" "));

        List<String> rolesList = user.getRoles()
            .stream()
            .map(Role::getName)
            .toList();

        var claims = JwtClaimsSet.builder()
            .issuer("dellasse.com")
            .expiresAt(now.plusSeconds(expiresIn))
            .subject(user.getUuid().toString())
            .claim("roles", scope)
            .build();

        var jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims));
        var jwtValue = jwt.getTokenValue();

        Optional<User> userOptional = userRepository.findByUsername(loginRequest.username());

        if (userOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("token", jwtValue);
        response.put("roles", rolesList);
        return ResponseEntity.ok(response);
    }

    /**
     * Define os valores padrão para um usuário.
     *
     * @param user O usuário a ser configurado.
     */
    private void defaultValues(User user){
        user.setActive(true);
        Role role = roleRepository.findById(Role.Values.BASIC.getRoleId())
            .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.setRoles(List.of(role));

    }

    /**
     * Atualiza um usuário existente.
     *
     * @param request  Dados atualizados do usuário.
     * @param userId   ID do usuário a ser atualizado.
     * @param token    Token do usuário que está realizando a atualização.
     * @return Resposta HTTP indicando o resultado da operação.
     */
    public  ResponseEntity<?>  updateUser(UserUpdateRequest request, UUID userId, String token){
        UUID userUUID = ConvertString.toUUID(token);
        
        if (userUUID == null){
            throw new DomainException(DomainError.USER_NOT_FOUND_INTERNAL);
        }

        User user = userRepository.findById(userUUID)
            .orElseThrow(() -> new DomainException(DomainError.USER_NOT_FOUND));

        if (!user.getUuid().equals(userUUID)){
            throw new DomainException(DomainError.USER_CANNOT_UPDATE_DATA);
        }

        user = setValuesUpdate(request, user);
        if (user == null){
            throw new DomainException(DomainError.USER_NOT_FOUND_INTERNAL);
        }

        User userSaved = userRepository.save(user);

        return ResponseEntity.ok(UserMapper.toResponse(userSaved));
    }

    /**
     * Valida a empresa associada ao usuário.
     *
     * @param userId ID do usuário.
     * @return ID da empresa associada ao usuário.
     */
    public UUID validateUserEnterprise(UUID userId){

        if (userId == null){
            throw new DomainException(DomainError.USER_NOT_FOUND_INTERNAL);
        }

        if (!userRepository.existsById(userId)){
            throw new DomainException(DomainError.USER_NOT_FOUND);
        }

        UUID enterpriseId = userRepository.findEnterpriseIdByUuid(userId);

        if (enterpriseId == null){
            return null;
        }

        if (!userRepository.existsByUuidAndEnterprise_Id(userId, enterpriseId)){
            throw new DomainException(DomainError.ENTERPRISE_FORBIDDEN);
        }

        return enterpriseId;
    }

    /**
     * Busca os papéis de um usuário.
     *
     * @param userId ID do usuário.
     * @return Lista de papéis do usuário.
     */
    public List<Role> getRoles(UUID userId){
        if (userId == null){
            throw new DomainException(DomainError.USER_NOT_FOUND_INTERNAL);
        }
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new DomainException(DomainError.USER_NOT_FOUND));

        return user.getRoles()
            .stream()
            .toList();
    }

    /**
     * Define os valores atualizados para um usuário.
     *
     * @param request Dados atualizados do usuário.
     * @param user    O usuário a ser atualizado.
     * @return O usuário atualizado.
     */
    private User setValuesUpdate(UserUpdateRequest request, User user){
        if(request.username() != null){
            if (request.username().isBlank()){
                throw new DomainException(DomainError.USER_REQUIRE_USERNAME);
            }
            if (userRepository.existsByUsername(request.username())) {
                throw new DomainException(DomainError.USER_ALREADY_EXISTS);
            }
            user.setName(request.name());
        }
     
        if (request.email() != null){
            if (userRepository.existsByEmail(request.email())) {
                throw new DomainException(DomainError.USER_EMAIL_ALREADY_EXISTS);
            }
            user.setEmail(request.email());
        }
   
        if (request.username() != null) user.setUsername(request.username());
        if (request.password() != null) user.setPassword(passwordEncoder.encode(request.password()));

        Boolean active = request.active();
        if (active != null) user.setActive(request.active());

        return user;
    }

    /**
     * Verifica se o usuário possui papel de staff (admin).
     *
     * @param roles Lista de papéis do usuário.
     * @return true se o usuário for staff, false caso contrário.
     */
    public boolean isStaff(List<Role> roles){
        if (roles == null || roles.isEmpty()){
            throw new NullArgumentException("Roles must contain at least one valid entry");
        }

        return roles.stream()
                .anyMatch(role -> role.getName().equals(Role.Values.ADMIN.getName()));
    }

    /**
     * Busca todos os usuários.
     *
     * @return Resposta HTTP contendo a lista de usuários.
     */
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> response = users.stream()
                .map(UserMapper::toUserResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
}

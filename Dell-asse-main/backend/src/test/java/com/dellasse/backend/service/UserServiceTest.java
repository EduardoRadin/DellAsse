package com.dellasse.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import com.dellasse.backend.contracts.user.UserCreateRequest;
import com.dellasse.backend.contracts.user.UserLoginRequest;
import com.dellasse.backend.exceptions.DomainException;
import com.dellasse.backend.models.Role;
import com.dellasse.backend.models.User;
import com.dellasse.backend.repositories.RoleRepository;
import com.dellasse.backend.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtEncoder jwtEncoder;

    @Test
    @DisplayName("Deve criar usuário com sucesso quando dados forem válidos")
    void createUser_Success() {
        // Arrange (Preparação)
        UserCreateRequest request = new UserCreateRequest(
            "Lucas", 
            "lucas@email.com", 
            "lucasuser", 
            "123456", 
            "123456", // CORREÇÃO: Adicionado confirmPassword
            "49999999999", // Telefone (11 dígitos)
            LocalDate.of(2000, 1, 1) // Data no passado
        );
        
        // Simula que não existe ninguém com esse user/email
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(roleRepository.findById(any())).thenReturn(Optional.of(new Role(3L, "BASIC")));
        when(passwordEncoder.encode(any())).thenReturn("senhaCriptografada");

        // Act (Ação)
        assertDoesNotThrow(() -> userService.createUser(request, null));

        // Assert (Verificação)
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar criar usuário com email existente")
    void createUser_EmailExists() {
        // Arrange
        UserCreateRequest request = new UserCreateRequest(
            "Lucas", 
            "lucas@email.com", 
            "lucasuser", 
            "123456", 
            "123456", // CORREÇÃO: Adicionado confirmPassword
            "49999999999", 
            LocalDate.of(2000, 1, 1)
        );

        when(userRepository.existsByUsername(request.username())).thenReturn(false);
        when(userRepository.existsByEmail(request.email())).thenReturn(true); // Email já existe

        // Act & Assert
        assertThrows(DomainException.class, () -> userService.createUser(request, null));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve realizar login e retornar token quando credenciais forem válidas")
    void loginUser_Success() {
        // Arrange
        String username = "lucasuser";
        String password = "123";
        UserLoginRequest loginRequest = new UserLoginRequest(username, password);

        User mockUser = new User();
        mockUser.setUuid(UUID.randomUUID());
        mockUser.setUsername(username);
        mockUser.setPassword("senhaCriptografada");
        mockUser.setRoles(List.of(new Role(3L, "BASIC")));

        when(userRepository.existsByUsername(username)).thenReturn(true);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(password, mockUser.getPassword())).thenReturn(true);
        
        // Mock do JWT
        Jwt jwt = mock(Jwt.class);
        when(jwt.getTokenValue()).thenReturn("token.jwt.falso");
        when(jwtEncoder.encode(any())).thenReturn(jwt);

        // Act
        var response = userService.loginUser(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
    }
}
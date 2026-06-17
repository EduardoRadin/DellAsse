package com.dellasse.backend.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dellasse.backend.models.Enterprise;
import com.dellasse.backend.models.Role;
import com.dellasse.backend.models.User;
import com.dellasse.backend.repositories.EnterpriseRepository;
import com.dellasse.backend.repositories.RoleRepository;
import com.dellasse.backend.repositories.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(null, "ADMIN"));
            roleRepository.save(new Role(null, "FUNCIONARIO"));
            roleRepository.save(new Role(null, "BASIC"));
        }

        if (enterpriseRepository.count() == 0) {
            enterpriseRepository.save(new Enterprise("dellasse", "12334567890", "rua das flores, 123", "99999-999", "interno@interno.com", "", LocalDateTime.now().plusDays(30)));
        }

        if(userRepository.count() == 0){
           userRepository.save(new User(
                "teste", 
                "teste@teste.com", 
                "teste", 
                passwordEncoder.encode("123456"),
                LocalDate.of(2000, 1, 1),
                "49984008237",
                true, 
                List.of(roleRepository.findByName("BASIC")),
                enterpriseRepository.findAll().stream().findFirst().orElse(null)
            ));
        }
    }
}

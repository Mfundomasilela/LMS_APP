package com.example.LMS_APP.config;

import com.example.LMS_APP.entity.User;
import com.example.LMS_APP.entity.UserRole;
import com.example.LMS_APP.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seed(UserRepository userRepo, PasswordEncoder encoder) {
        return args -> {
            userRepo.findByUsername("nomfundo").orElseGet(() ->
                    userRepo.save(new User("nomfundo", encoder.encode("Password@123"), UserRole.EMPLOYEE, "Nomfundo"))
            );

            userRepo.findByUsername("manager").orElseGet(() ->
                    userRepo.save(new User("manager", encoder.encode("Password@123"), UserRole.MANAGER, "Ms Dlamini"))
            );
        };
    }
}

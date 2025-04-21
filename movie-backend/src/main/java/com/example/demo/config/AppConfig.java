package com.example.demo.config;


import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

//    @Bean
//    public User createDefaultAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        return userRepository.findByUsername("admin")
//                .orElseGet(() -> {
//                    User admin = new User(null, "yasmeen", "saad","admin@mail.com", passwordEncoder.encode("admin123"), User.Role.ADMIN);
//                    User savedAdmin = userRepository.save(admin);
//                    System.out.println("✅ Default admin user created: admin@mail.com / admin123");
//                    return savedAdmin;
//                });
//    }
}

package com.binar.byteacademy.seeders;

import com.binar.byteacademy.entity.User;
import com.binar.byteacademy.enumeration.EnumRole;
import com.binar.byteacademy.enumeration.EnumStatus;
import com.binar.byteacademy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedAdmin();
        seedCustomer();
    }

    private void seedAdmin() {
        String adminUsername = "admin";
        String adminPassword = "password";

        userRepository.findFirstByUsername(adminUsername).ifPresentOrElse(
                user -> {
                    user.setPassword(passwordEncoder.encode(adminPassword));
                    userRepository.save(user);
                },
                () -> {
                    User admin = User.builder()
                            .username(adminUsername)
                            .email("admin@gmail.com")
                            .phoneNumber("081234567890")
                            .password(passwordEncoder.encode(adminPassword))
                            .role(EnumRole.ADMIN)
                            .status(EnumStatus.ACTIVE)
                            .isVerifiedEmail(true)
                            .isVerifiedPhoneNumber(true)
                            .build();
                    userRepository.save(admin);
                });
    }

    private void seedCustomer(){
        String customerUsername = "customer";
        String customerPassword = "password";

        userRepository.findFirstByUsername(customerUsername).ifPresentOrElse(
                user -> {
                    user.setPassword(passwordEncoder.encode(customerPassword));
                    userRepository.save(user);
                },
                () -> {
                    User customer = User.builder()
                            .username(customerUsername)
                            .email("resarisyan@gmail.com")
                            .phoneNumber("081234567899")
                            .password(passwordEncoder.encode(customerPassword))
                            .role(EnumRole.ADMIN)
                            .status(EnumStatus.ACTIVE)
                            .isVerifiedEmail(true)
                            .isVerifiedPhoneNumber(true)
                            .build();
                    userRepository.save(customer);
                });
    }
}

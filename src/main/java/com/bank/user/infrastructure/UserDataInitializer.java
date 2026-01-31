package com.bank.user.infrastructure;

import com.bank.user.domain.Role;
import com.bank.user.domain.User;
import com.bank.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class UserDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {

            User user = new User();
            user.setLogin("testUser");
            user.setEmail("test@bank.com");
            user.setPassword(passwordEncoder.encode("password1111"));
            user.setRole(Role.USER);
            user.setEnabled(true);

            userRepository.save(user);
        }
    }
}

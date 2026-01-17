package com.bank.security;

import com.bank.security.auth.AuthService;
import com.bank.user.domain.Role;
import com.bank.user.domain.User;
import com.bank.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class AuthServiceIntegrationTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    @Test
    void shouldLoginAndReturnJwtToken() {
        /// given
        User user = new User();
        user.setLogin("testUser");
        user.setEmail("test@test.com");
        user.setPassword(passwordEncoder.encode("password111111"));
        user.setRole(Role.USER);
        user.setEnabled(true);

        userRepository.save(user);

        /// when
        String token = authService.login("testUser", "password111111");

        /// then
        assertThat(token).isNotBlank();
        assertThat(token.split("\\.")).hasSize(3);
    }
}

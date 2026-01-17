package com.bank;

import com.bank.user.domain.Role;
import com.bank.user.domain.User;
import com.bank.user.domain.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BankSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankSystemApplication.class, args);
	}

	@Bean
	@org.springframework.context.annotation.Profile("dev")
	CommandLineRunner createInitialUsers(
			UserRepository userRepository,
			PasswordEncoder passwordEncoder
	) {
		return args -> {

			if (userRepository.findByLogin("admin").isEmpty()) {
				User admin = new User();
				admin.setLogin("admin");
				admin.setEmail("admin@bank.local");
				admin.setPassword(passwordEncoder.encode("admin123"));
				admin.setRole(Role.ADMIN);
				admin.setEnabled(true);
				userRepository.save(admin);
			}

			if (userRepository.findByLogin("testUser").isEmpty()) {
				User user = new User();
				user.setLogin("testUser");
				user.setEmail("test@test.com");
				user.setPassword(passwordEncoder.encode("password111111"));
				user.setRole(Role.USER);
				user.setEnabled(true);
				userRepository.save(user);
			}
		};
	}
}

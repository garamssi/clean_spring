package tobyspring.splearn.adapter.security;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SecurePasswordEncoderTest {

	@Test
	void securePasswordEncoderTest() {
		SecurePasswordEncoder securePasswordEncoder = new SecurePasswordEncoder();
		String passwordHash = "password123";
		String encodedPassword = securePasswordEncoder.encode(passwordHash);

		assertNotNull(encodedPassword);
		assertThat(securePasswordEncoder.matches(passwordHash, encodedPassword)).isTrue();
		assertThat(securePasswordEncoder.matches("wrongpassword", encodedPassword)).isFalse();
	}
}
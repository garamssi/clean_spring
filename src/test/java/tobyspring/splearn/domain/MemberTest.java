package tobyspring.splearn.domain;


import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemberTest {

	Member member;

	PasswordEncoder passwordEncoder;

	@BeforeEach
	void setUp() {
		// Any setup code if needed
		passwordEncoder = new PasswordEncoder() {
			@Override
			public String encode(String password) {
				return password.toUpperCase();
			}

			@Override
			public boolean matches(String password, String passwordHash) {
				return encode(password).equals(passwordHash);
			}
		};

		member = Member.create("test@test", "test", "secret", passwordEncoder);
	}

	@Test
	void createMember() {
		var member = Member.create("test@test.com", "test", "secret", passwordEncoder);

		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
	}

	@Test
	void activate() {
	    var member = Member.create("test@test.com", "test", "secret", passwordEncoder);
		member.activate();

		assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
	}

	@Test
	void activateFail() {
		var member = Member.create("test@test.com", "test", "secret", passwordEncoder);
		member.activate();

		assertThatThrownBy(() -> member.activate())
			.isInstanceOf(IllegalStateException.class);
	}

	@Test
	void deactivate() {
		var member = Member.create("test@test.com", "test", "secret", passwordEncoder);
		member.activate();

		member.deactivate();

		assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);

	}

	@Test
	void deactivateFail() {
		var member = Member.create("test@test.com", "test", "secret", passwordEncoder);

		assertThatThrownBy(member::deactivate)
			.isInstanceOf(IllegalStateException.class);

		member.activate();
		member.deactivate();

		assertThatThrownBy(member::deactivate)
			.isInstanceOf(IllegalStateException.class);
	}

	@Test
	void verifyPassword() {
		assertThat(member.verifyPassword("secret", passwordEncoder)).isTrue();
		assertThat(member.verifyPassword("test", passwordEncoder)).isFalse();
	}

	@Test
	void changeNickname() {
		assertThat(member.getNickname()).isEqualTo("test");

		member.changeNickname("newNickname");

		assertThat(member.getNickname()).isEqualTo("newNickname");
	}

	@Test
	void changePassword() {
		member.changePassword("newSecret", passwordEncoder);

		assertThat(member.verifyPassword("newSecret", passwordEncoder)).isTrue();
	}

}
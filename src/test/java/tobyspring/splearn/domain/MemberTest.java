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

		member = Member.create(new MemberCreateRequest("test@test", "test", "secret"), passwordEncoder);
	}

	@Test
	void createMember() {

		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
	}

	@Test
	void activate() {
		member.activate();

		assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
	}

	@Test
	void activateFail() {
		member.activate();

		assertThatThrownBy(() -> member.activate())
			.isInstanceOf(IllegalStateException.class);
	}

	@Test
	void deactivate() {
		member.activate();

		member.deactivate();

		assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);

	}

	@Test
	void deactivateFail() {

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

	@Test
	void isActive() {
		assertThat(member.isActive()).isFalse();

		member.activate();

		assertThat(member.isActive()).isTrue();

		member.deactivate();

		assertThat(member.isActive()).isFalse();
	}

	@Test
	void isValidEmail() {
		assertThatThrownBy(() -> Member.create(new MemberCreateRequest("invalid-email", "test", "secret"), passwordEncoder))
			.isInstanceOf(IllegalArgumentException.class);

		Member.create(new MemberCreateRequest("test@test", "test", "secret"), passwordEncoder);
	}

}
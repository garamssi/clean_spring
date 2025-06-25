package tobyspring.splearn.domain;


import static org.assertj.core.api.Assertions.*;
import static tobyspring.splearn.domain.MemberFixture.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemberTest {

	Member member;

	PasswordEncoder passwordEncoder;

	@BeforeEach
	void setUp() {
		// Any setup code if needed
		passwordEncoder = createPasswordEncoder();

		member = Member.register(createMemberRegisterRequest(), passwordEncoder);
	}

	@Test
	void registerMember() {

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
		assertThat(member.verifyPassword("longsecret", passwordEncoder)).isTrue();
		assertThat(member.verifyPassword("bravojay", passwordEncoder)).isFalse();
	}

	@Test
	void changeNickname() {
		assertThat(member.getNickname()).isEqualTo("bravojay");

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
		assertThatThrownBy(() -> Member.register(createMemberRegisterRequest("invalid"), passwordEncoder))
			.isInstanceOf(IllegalArgumentException.class);

		Member.register(createMemberRegisterRequest(), passwordEncoder);
	}

}
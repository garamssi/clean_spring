package tobyspring.splearn.application.provided;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.DuplicateEmailException;
import tobyspring.splearn.domain.Member;
import tobyspring.splearn.domain.MemberFixture;
import tobyspring.splearn.domain.MemberRegisterRequest;
import tobyspring.splearn.domain.MemberStatus;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
record MemberRegisterTest(MemberRegister memberRegister, EntityManager entityManager) {

	@Test
	void register() {
		Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

		assertThat(member.getId()).isNotNull();
		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
	}

	@Test
	void duplicateEmailFail() {
		memberRegister.register(MemberFixture.createMemberRegisterRequest());

		assertThatThrownBy(() -> memberRegister.register(MemberFixture.createMemberRegisterRequest()))
			.isInstanceOf(DuplicateEmailException.class);
	}

	@Test
	void activate() {
		Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
		entityManager.flush();
		entityManager.clear();

		member = memberRegister.activate(member.getId());

		entityManager.flush();

		assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
	}

	@Test
	void memberResisterRequestFail() {
		checkValidation(new MemberRegisterRequest("test@test.com", "test", "1234567111"));
		checkValidation(new MemberRegisterRequest("tt", "test123", "1234567111"));
		checkValidation(new MemberRegisterRequest("test@test.com", "test123", "1"));

	}

	private void checkValidation(MemberRegisterRequest invalid) {
		assertThatThrownBy(() -> memberRegister.register(invalid))
			.isInstanceOf(ConstraintViolationException.class);
	}

}
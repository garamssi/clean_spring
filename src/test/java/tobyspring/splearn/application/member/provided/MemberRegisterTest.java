package tobyspring.splearn.application.member.provided;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.application.member.required.MemberRegister;
import tobyspring.splearn.domain.member.DuplicateEmailException;
import tobyspring.splearn.domain.member.DuplicateProfileException;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberFixture;
import tobyspring.splearn.domain.member.MemberInfoUpdateRequest;
import tobyspring.splearn.domain.member.MemberRegisterRequest;
import tobyspring.splearn.domain.member.MemberStatus;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
record MemberRegisterTest(MemberRegister memberRegister, EntityManager entityManager) {

	@Test
	void register() {
		Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

		System.out.println(member);

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
	void memberResisterRequestFail() {
		checkValidation(new MemberRegisterRequest("test@test.com", "test", "1234567111"));
		checkValidation(new MemberRegisterRequest("tt", "test123", "1234567111"));
		checkValidation(new MemberRegisterRequest("test@test.com", "test123", "1"));

	}

	private void checkValidation(MemberRegisterRequest invalid) {
		assertThatThrownBy(() -> memberRegister.register(invalid))
			.isInstanceOf(ConstraintViolationException.class);
	}

	@Test
	void activate() {
		Member member = registerMember();

		member = memberRegister.activate(member.getId());
		entityManager.flush();

		assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
		assertThat(member.getDetail().getActivatedAt()).isNotNull();
	}

	private Member registerMember() {
		Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
		entityManager.flush();
		entityManager.clear();
		return member;
	}

	private Member registerMember(String email) {
		Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest(email));
		entityManager.flush();
		entityManager.clear();
		return member;
	}

	@Test
	void deactivate() {
		Member member = registerMember();

		memberRegister.activate(member.getId());
		entityManager.flush();
		entityManager.clear();

		member = memberRegister.deactivate(member.getId());

		assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
		assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
	}

	@Test
	void updateInfo() {
		Member member = registerMember();

		memberRegister.activate(member.getId());
		entityManager.flush();
		entityManager.clear();

		member = memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("Peter", "toby100", "자기소개"));

		assertThat(member.getDetail().getProfile().address()).isEqualTo("toby100");
	}

	@Test
	void updateInfoFail() {
		Member member = registerMember();
		memberRegister.activate(member.getId());
		memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("Peter", "toby100", "자기소개"));

		Member member2 = registerMember("toby2@splearn.app");
		memberRegister.activate(member2.getId());
		entityManager.flush();
		entityManager.clear();

		// member2는 기존의 member와 같은 프로필 주소를 사용할 수 없다
		assertThatThrownBy(() -> {
			memberRegister.updateInfo(member2.getId(), new MemberInfoUpdateRequest("James", "toby100", "Introduction"));
		}).isInstanceOf(DuplicateProfileException.class);

		// 다른 프로필 주소로는 변경 가능
		memberRegister.updateInfo(member2.getId(), new MemberInfoUpdateRequest("James", "toby1011", "Introduction"));

		// 기존 프로필 주소를 바꾸는 것도 가능
		memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("James", "toby1001", "Introduction"));

		// 프로필 주소를 제거하는 것도 가능
		memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("James", "", "Introduction"));

		// 프로필 주소 중복는 허용하지 않음
		assertThatThrownBy(() -> {
			memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("James", "toby1011", "Introduction"));
		}).isInstanceOf(DuplicateProfileException.class);

	}


}
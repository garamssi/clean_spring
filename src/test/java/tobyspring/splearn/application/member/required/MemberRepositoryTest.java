package tobyspring.splearn.application.member.required;

import static org.assertj.core.api.Assertions.*;
import static tobyspring.splearn.domain.member.MemberFixture.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import jakarta.persistence.EntityManager;
import tobyspring.splearn.application.member.provided.MemberRepository;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberStatus;

@DataJpaTest // JPA 테스트를 위한 어노테이션
class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	EntityManager entityManager;

	@Test
	void createMember() {
		Member member = Member.register(createMemberRegisterRequest(), createPasswordEncoder());

		assertThat(member.getId()).isNull();

		memberRepository.save(member);

		assertThat(member.getId()).isNotNull();

		entityManager.flush();
		entityManager.clear();

		Member found = memberRepository.findById(member.getId()).orElseThrow();
		assertThat(found.getStatus()).isEqualTo(MemberStatus.PENDING);
		assertThat(found.getDetail().getRegisteredAt()).isNotNull();
	}

	@Test
	void duplicateEmailFail() {
		Member member = Member.register(createMemberRegisterRequest(), createPasswordEncoder());

		memberRepository.save(member);

		Member member2 = Member.register(createMemberRegisterRequest(), createPasswordEncoder());
		assertThatThrownBy(() -> memberRepository.save(member2))
			.isInstanceOf(DataIntegrityViolationException.class);
	}

}
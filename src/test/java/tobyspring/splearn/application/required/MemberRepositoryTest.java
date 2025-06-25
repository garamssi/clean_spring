package tobyspring.splearn.application.required;

import static org.assertj.core.api.Assertions.*;
import static tobyspring.splearn.domain.MemberFixture.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.EntityManager;
import tobyspring.splearn.domain.Member;

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
	}


}
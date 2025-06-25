package tobyspring.splearn.application.required;

import org.springframework.data.repository.Repository;

import tobyspring.splearn.domain.Member;

/**
 * 회원 정보를 저장하고 조회하는 기능을 제공한다.
 */
public interface MemberRepository extends Repository<Member, Long> {
	Member save(Member member);
}

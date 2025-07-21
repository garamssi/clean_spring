package tobyspring.splearn.application.member.provided;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import tobyspring.splearn.domain.shared.Email;
import tobyspring.splearn.domain.member.Member;

/**
 * 회원 정보를 저장하고 조회하는 기능을 제공한다.
 */
public interface MemberRepository extends Repository<Member, Long> {
	Member save(Member member);

	Optional<Member> findByEmail(Email email);

	Optional<Member> findById(Long memberId);
}

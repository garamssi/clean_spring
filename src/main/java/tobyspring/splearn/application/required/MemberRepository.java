package tobyspring.splearn.application.required;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.fasterxml.jackson.databind.introspect.AnnotationCollector;

import tobyspring.splearn.domain.Email;
import tobyspring.splearn.domain.Member;

/**
 * 회원 정보를 저장하고 조회하는 기능을 제공한다.
 */
public interface MemberRepository extends Repository<Member, Long> {
	Member save(Member member);

	Optional<Member> findByEmail(Email email);

	Optional<Member> findById(Long memberId);
}

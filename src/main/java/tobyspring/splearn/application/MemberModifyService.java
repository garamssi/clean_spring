package tobyspring.splearn.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import lombok.RequiredArgsConstructor;
import tobyspring.splearn.application.provided.MemberFinder;
import tobyspring.splearn.application.provided.MemberRegister;
import tobyspring.splearn.application.required.EmailSender;
import tobyspring.splearn.application.required.MemberRepository;
import tobyspring.splearn.domain.DuplicateEmailException;
import tobyspring.splearn.domain.Email;
import tobyspring.splearn.domain.Member;
import tobyspring.splearn.domain.MemberRegisterRequest;
import tobyspring.splearn.domain.PasswordEncoder;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberModifyService implements MemberRegister {

	private final MemberFinder memberFinder;
	private final MemberRepository memberRepository;
	private final EmailSender emailSender;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Member register(MemberRegisterRequest registerRequest) {
		// check
		checkDuplicateEmail(registerRequest);

		// domain model
		Member member = Member.register(registerRequest, passwordEncoder);

		// repository
		memberRepository.save(member);

		// post process
		sendWelcomeEmail(member);

		return member;
	}

	@Override
	public Member activate(Long memberId) {
		Member member = memberFinder.find(memberId);

		member.activate();

		return memberRepository.save(member);
	}

	private void sendWelcomeEmail(Member member) {
		emailSender.send(member.getEmail(), "등록을 완료해주세요.", "아래 링크를 클릭해서 등록을 완료햐주세요.");
	}

	private void checkDuplicateEmail(MemberRegisterRequest registerRequest) {
		if (memberRepository.findByEmail(new Email((registerRequest.email()))).isPresent()) {
			throw new DuplicateEmailException("이미 등록된 이메일입니다: " + registerRequest.email());
		}
	}
}

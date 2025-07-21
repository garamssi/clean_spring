package tobyspring.splearn;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import tobyspring.splearn.application.member.required.EmailSender;
import tobyspring.splearn.domain.member.MemberFixture;
import tobyspring.splearn.domain.member.PasswordEncoder;

@TestConfiguration
public class SplearnTestConfiguration {

	@Bean
	public EmailSender emailSender() {
		return (email, subject, message) -> {
			// 이메일 전송 로직을 구현하지 않고 테스트를 위해 빈으로 등록
			// 실제 이메일 전송 로직은 구현하지 않음
			System.out.println("Sending Email: " + email);
		};
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return MemberFixture.createPasswordEncoder();
	}
}

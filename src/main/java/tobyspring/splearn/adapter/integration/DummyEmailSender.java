package tobyspring.splearn.adapter.integration;

import org.springframework.stereotype.Component;

import tobyspring.splearn.application.required.EmailSender;
import tobyspring.splearn.domain.Email;

@Component
public class DummyEmailSender implements EmailSender {

	@Override
	public void send(Email email, String subject, String body) {
		// 이메일 전송 로직을 구현하지 않고 테스트를 위해 빈으로 등록
		// 실제 이메일 전송 로직은 구현하지 않음
		System.out.println("Dummy Sending Email: " + email);
	}
}

package tobyspring.splearn.application.member.required;

import tobyspring.splearn.domain.shared.Email;

/**
 * 이메일을 전송하는 기능을 제공한다.
 */
public interface EmailSender {
	void send(Email email, String subject, String body);
}

package tobyspring.splearn.adapter.integration;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIn;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import tobyspring.splearn.domain.Email;

class DummyEmailSenderTest {

	@Test
	@StdIo
	void dummyEmailSender(StdOut out) {
		DummyEmailSender sender = new DummyEmailSender();
		sender.send(new Email("test@test.com"), "Test Subject", "Test Body");

		assertThat(out.capturedLines()[0]).isEqualTo("Dummy Sending Email: Email[address=test@test.com]");
	}

}
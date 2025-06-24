package tobyspring.splearn.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EmailTest {

	@Test
	void equality() {
		var email1 = new Email("roby@splearp.app");
		var email2 = new Email("roby@splearp.app");

		assertThat(email1).isEqualTo(email2);
	}

}
package tobyspring.splearn.domain.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ProfileTest {
	@Test
	void profile() {
		new Profile("tobyilee");
		new Profile("toby100");
		new Profile("12345");
		new Profile("");
	}

	@Test
	void profileFail() {
		assertThatThrownBy(() -> new Profile("toolongtoolongtoolong")).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> new Profile("A")).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> new Profile("프로필")).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void url() {
		var profile = new Profile("tobyilee");

		assertThat(profile.url()).isEqualTo("@tobyilee");
	}
}
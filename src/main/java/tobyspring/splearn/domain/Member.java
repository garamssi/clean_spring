package tobyspring.splearn.domain;

import java.util.Objects;

import org.springframework.util.Assert;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Member {
	private String email;

	private String nickname;

	private String passwordHash;

	private MemberStatus status;

	private Member(String email, String nickname, String passwordHash) {
		this.email = Objects.requireNonNull(email);
		this.nickname = Objects.requireNonNull(nickname);
		this.passwordHash = Objects.requireNonNull(passwordHash);

		this.status = MemberStatus.PENDING;
	}

	public static Member create(String email, String nickname, String password, PasswordEncoder passwordEncoder) {
		return new Member(email, nickname, passwordEncoder.encode(password));
	}

	public void activate() {
		// if (status != MemberStatus.PENDING) throw new IllegalStateException("PENDING 상태가 아닙니다.");
		Assert.state(status == MemberStatus.PENDING, "PENDING 상태가 아닙니다.");

		this.status = MemberStatus.ACTIVE;
	}

	public void deactivate() {
		Assert.state(status == MemberStatus.ACTIVE, "ACTIVE 상태가 아닙니다.");

		this.status = MemberStatus.DEACTIVATED;
	}

	public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
		return passwordEncoder.matches(password, passwordHash);
	}

	public void changeNickname(String nickname) {
		this.nickname = nickname;
	}

	public void changePassword(String password, PasswordEncoder passwordEncoder) {
		this.passwordHash = passwordEncoder.encode(password);
	}
}

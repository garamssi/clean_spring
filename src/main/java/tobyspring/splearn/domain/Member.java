package tobyspring.splearn.domain;

import static java.util.Objects.*;

import java.util.regex.Pattern;

import org.springframework.util.Assert;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Member {
	private Email email;

	private String nickname;

	private String passwordHash;

	private MemberStatus status;

	private Member(){}

	public static Member create(MemberCreateRequest createRequest, PasswordEncoder passwordEncoder) {
		Member member = new Member();

		member.email = new Email(createRequest.email());
		member.nickname = requireNonNull(createRequest.nickname());
		member.passwordHash = passwordEncoder.encode(requireNonNull(createRequest.password()));

		member.status = MemberStatus.PENDING;

		return member;
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
		this.nickname = requireNonNull(nickname);
	}

	public void changePassword(String password, PasswordEncoder passwordEncoder) {
		this.passwordHash = passwordEncoder.encode(requireNonNull(password));
	}

	public boolean isActive() {
		return status == MemberStatus.ACTIVE;
	}
}

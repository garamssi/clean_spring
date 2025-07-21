package tobyspring.splearn.domain.member;

import static java.util.Objects.*;
import static org.springframework.util.Assert.*;

import java.util.Objects;

import org.hibernate.annotations.NaturalId;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tobyspring.splearn.domain.AbstractEntity;
import tobyspring.splearn.domain.shared.Email;

@Entity
@Getter
@ToString(callSuper = true, exclude = "detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends AbstractEntity {

	@NaturalId // hibernate에서 자연 키로 사용
	private Email email;

	private String nickname;

	private String passwordHash;

	private MemberStatus status;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private MemberDetail detail;

	public static Member register(MemberRegisterRequest registerRequest, PasswordEncoder passwordEncoder) {
		Member member = new Member();

		member.email = new Email(registerRequest.email());
		member.nickname = requireNonNull(registerRequest.nickname());
		member.passwordHash = passwordEncoder.encode(requireNonNull(registerRequest.password()));

		member.status = MemberStatus.PENDING;

		member.detail = MemberDetail.create();

		return member;
	}

	public void activate() {
		// if (status != MemberStatus.PENDING) throw new IllegalStateException("PENDING 상태가 아닙니다.");
		state(status == MemberStatus.PENDING, "PENDING 상태가 아닙니다.");

		this.status = MemberStatus.ACTIVE;
		this.detail.activate();
	}

	public void deactivate() {
		state(status == MemberStatus.ACTIVE, "ACTIVE 상태가 아닙니다.");

		this.status = MemberStatus.DEACTIVATED;
		this.detail.deactivate();
	}

	public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
		return passwordEncoder.matches(password, passwordHash);
	}

	public void changeNickname(String nickname) {
		this.nickname = requireNonNull(nickname);
	}

	public void updateInfo(MemberInfoUpdateRequest updateRequest) {
		state(getStatus() == MemberStatus.ACTIVE, "등록 완료 상태가 아니면 정보를 수정할 수 없습니다");

		this.nickname = Objects.requireNonNull(updateRequest.nickname());

		this.detail.updateInfo(updateRequest);
	}

	public void changePassword(String password, PasswordEncoder passwordEncoder) {
		this.passwordHash = passwordEncoder.encode(requireNonNull(password));
	}

	public boolean isActive() {
		return status == MemberStatus.ACTIVE;
	}
}

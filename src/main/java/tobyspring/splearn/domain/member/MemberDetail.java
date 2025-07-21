package tobyspring.splearn.domain.member;

import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.NaturalIdCache;
import org.springframework.util.Assert;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tobyspring.splearn.domain.AbstractEntity;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NaturalIdCache
public class MemberDetail extends AbstractEntity {
	@Embedded
	private Profile profile;

	private String introduction;

	private LocalDateTime registeredAt;

	private LocalDateTime activatedAt;

	private LocalDateTime deactivatedAt;

	static MemberDetail create() {
		MemberDetail memberDetail = new MemberDetail();
		memberDetail.registeredAt = LocalDateTime.now();
		return memberDetail;
	}

	void activate() {
		Assert.state(activatedAt == null, "이미 activatedAt은 설정되었습니다");
		this.activatedAt = LocalDateTime.now();
	}

	void deactivate() {
		Assert.state(deactivatedAt == null, "이미 deactivatedAt은 설정되었습니다");
		this.deactivatedAt = LocalDateTime.now();
	}

	void updateInfo(MemberInfoUpdateRequest updateRequest) {
		this.profile = new Profile(updateRequest.profileAddress());
		this.introduction = Objects.requireNonNull(updateRequest.introduction());
	}
}

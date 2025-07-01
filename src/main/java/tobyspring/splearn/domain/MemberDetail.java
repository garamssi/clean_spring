package tobyspring.splearn.domain;

import static java.util.Objects.*;

import java.time.LocalDateTime;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.springframework.util.Assert;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NaturalIdCache
public class MemberDetail extends AbstractEntity {
	private String profile;

	private String intoroduction;

	private LocalDateTime regiteredAt;

	private LocalDateTime activatedAt;

	private LocalDateTime deactivatedAt;
}

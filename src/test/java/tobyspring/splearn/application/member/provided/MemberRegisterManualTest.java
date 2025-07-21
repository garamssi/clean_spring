// package tobyspring.splearn.application.member.provided;
//
// import static org.assertj.core.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.*;
//
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;
//
// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
// import org.springframework.test.util.ReflectionTestUtils;
//
// import tobyspring.splearn.application.MemberService;
// import tobyspring.splearn.application.member.EmailSender;
// import tobyspring.splearn.application.member.provided.MemberRepository;
// import tobyspring.splearn.domain.shared.Email;
// import tobyspring.splearn.domain.member.Member;
// import tobyspring.splearn.domain.member.MemberFixture;
// import tobyspring.splearn.domain.member.MemberStatus;
//
// class MemberRegisterManualTest {
//
// 	@Test
// 	void registerTestStub() {
// 		MemberRegister register = new MemberService(new MemberRepositoryStub(), new EmailSenderStub(), MemberFixture.createPasswordEncoder());
//
// 		Member member = register.register(MemberFixture.createMemberRegisterRequest());
//
// 		assertThat(member.getId()).isNotNull();
// 		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
// 	}
//
// 	/**
// 	 *
// 	 * Mock은 단순하게 테스트를 하게 하는 것을 넘어서 오브젝트하고 테스트하는 도중에 인프라와 어떤 상호 작용이 있었는 지까지 검증
// 	 */
// 	@Test
// 	void registerTestMock() {
// 		EmailSenderMock emailSenderMock = new EmailSenderMock();
// 		MemberRegister register = new MemberService(new MemberRepositoryStub(), emailSenderMock, MemberFixture.createPasswordEncoder());
//
// 		Member member = register.register(MemberFixture.createMemberRegisterRequest());
//
// 		assertThat(member.getId()).isNotNull();
// 		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
//
// 		assertThat(emailSenderMock.getEmail()).hasSize(1);
// 		assertThat(emailSenderMock.getEmail().get(0)).isEqualTo(member.getEmail());
//
//
// 	}
//
// 	/**
// 	 * Mockito를 사용한 테스트
// 	 */
// 	@Test
// 	void registerTestMockito() {
// 		EmailSenderMock emailSenderMock = Mockito.mock(EmailSenderMock.class);
// 		MemberRegister register = new MemberService(new MemberRepositoryStub(), emailSenderMock, MemberFixture.createPasswordEncoder());
//
// 		Member member = register.register(MemberFixture.createMemberRegisterRequest());
//
// 		assertThat(member.getId()).isNotNull();
// 		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
//
// 		Mockito.verify(emailSenderMock).send(eq(member.getEmail()), any(), any());
// 	}
//
// 	static class MemberRepositoryStub implements MemberRepository {
// 		@Override
// 		public Member save(Member member) {
// 			ReflectionTestUtils.setField(member, "id", 1L);
// 			return member;
// 		}
//
// 		@Override
// 		public Optional<Member> findByEmail(Email email) {
// 			return Optional.empty();
// 		}
//
// 		@Override
// 		public Optional<Member> findById(Long memberId) {
// 			return Optional.empty();
// 		}
// 	}
//
// 	static class EmailSenderStub implements EmailSender {
// 		@Override
// 		public void send(Email email, String subject, String body) {
//
// 		}
// 	}
//
// 	static class EmailSenderMock implements EmailSender {
// 		List<Email> tos = new ArrayList<>();
//
// 		public List<Email> getEmail() {
// 			return tos;
// 		}
//
// 		@Override
// 		public void send(Email email, String subject, String body) {
// 			tos.add(email);
// 		}
// 	}
//
//
//
//
// }
package tobyspring.splearn;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

class SplearnApplicationTest {

	@Test
	void run() {
		try(MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {

			SplearnApplication.main(new String[0]);

			mocked.verify(() -> SpringApplication.run(SplearnApplication.class, new String[0]));
		}
	}

}
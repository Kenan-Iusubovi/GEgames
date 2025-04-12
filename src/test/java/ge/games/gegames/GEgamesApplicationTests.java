package ge.games.gegames;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
		"jwt.at.secret=test-secret",
		"jwt.rt.secret=test-secret"
})
class GEgamesApplicationTests {

	@Test
	void contextLoads() {
	}

}

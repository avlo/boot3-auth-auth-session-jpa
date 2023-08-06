
import com.prosilion.scdecisionmatrix.config.AppUserAuthoritiesPopulator;
import com.prosilion.scdecisionmatrix.config.DatabaseConfig;
import com.prosilion.scdecisionmatrix.config.LdapUserConfig;
import com.prosilion.scdecisionmatrix.config.WebSecurityConfig;
import com.prosilion.scdecisionmatrix.service.MessageService;
import com.prosilion.scdecisionmatrix.service.security.jdbc.AuthUserDetailServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest
@WebAppConfiguration
@ContextConfiguration(classes = {WebSecurityConfig.class, DatabaseConfig.class, MessageService.class, AppUserAuthoritiesPopulator.class, LdapUserConfig.class, AuthUserDetailServiceImpl.class})
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@WithMockUser(username="user", password="user", roles={"USER"})
public class MessageServiceTest {

	@Autowired
	private MessageService messageService;

	@Test
	public void testGetSubscriptionMessage() {
		Assertions.assertEquals(messageService.getSubscriptionMessage(), "TEST MESSAGE");
	}

	@Test
	public void testThrowsException() {
		Assertions.assertThrowsExactly(NumberFormatException.class, () -> {
			messageService.throwMyException();
		});
	}
}

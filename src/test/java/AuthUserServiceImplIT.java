
import com.prosilion.scdecisionmatrix.service.MessageService;
import com.prosilion.scdecisionmatrix.service.security.AuthUserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
//@ContextConfiguration(classes = {AuthUserServiceImpl.class, AuthUserDetailServiceImpl.class})
@ContextConfiguration(classes = {MessageService.class, AuthUserServiceImpl.class})
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@WithMockUser(username="user", password="user", roles={"USER"})
public class AuthUserServiceImplIT {

//	@Autowired
//	private WebApplicationContext context;

	@Autowired
	private AuthUserServiceImpl authUserService;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testCreateUser() {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		Assertions.assertNotNull(authentication);
		Assertions.assertNotNull(authUserService);
	}

	@Test()
	public void testGetAllAppUsersAsDto() {
//		Assertions.assertNotNull(authUserService.getAllAppUsersAsDto());
	}

	///  FAILSAFE canary
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
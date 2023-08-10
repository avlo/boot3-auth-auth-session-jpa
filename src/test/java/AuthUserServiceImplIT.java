import com.prosilion.scdecisionmatrix.ScdecisionmatrixApplication;
import com.prosilion.scdecisionmatrix.model.dto.AppUserDto;
import com.prosilion.scdecisionmatrix.model.entity.AppUserAuthUser;
import com.prosilion.scdecisionmatrix.service.security.AuthUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ScdecisionmatrixApplication.class) // annotation specififically for INTEGRATION tests
@ActiveProfiles("test")
@WithMockUser(username="user", password="user", roles={"USER"})
public class AuthUserServiceImplIT {
	@Autowired
	private AuthUserService authUserService;

	@Test
	public void testCreateUser() {
		AppUserDto appUserDto = new AppUserDto();
		appUserDto.setUsername("test_user");
		appUserDto.setPassword("test_user_password");

		AppUserAuthUser appUserAuthUser = authUserService.createUser(appUserDto);
		Assertions.assertNotNull(appUserAuthUser);
		Assertions.assertEquals(authUserService.getAllAppUsersMappedAuthUsers().size(), 1);

		appUserDto.setUsername("another_test_user");
		appUserDto.setPassword("another_test_user_password");
		appUserAuthUser = authUserService.createUser(appUserDto);
		Assertions.assertNotNull(appUserAuthUser);

		Assertions.assertEquals(authUserService.getAllAppUsersMappedAuthUsers().size(), 2);
	}

	@Test()
	public void testGetAllAppUsersAsDto() {
		Assertions.assertNotNull(authUserService.getAllAppUsersAsDto());
	}

}
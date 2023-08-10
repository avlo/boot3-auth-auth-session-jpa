
import com.prosilion.scdecisionmatrix.service.MessageService;
import com.prosilion.scdecisionmatrix.service.security.jdbc.AuthUserDetailServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

	@Mock
	private AuthUserDetailServiceImpl mockAuthUserDetailService;
	@InjectMocks
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

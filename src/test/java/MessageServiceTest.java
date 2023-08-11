
import com.prosilion.scdecisionmatrix.service.MessageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

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

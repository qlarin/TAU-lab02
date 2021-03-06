package pl.edu.pjatk.s11617.lab02.app;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import pl.edu.pjatk.s11617.lab02.app.Messenger;
import pl.edu.pjatk.s11617.lab02.messenger.MessageServiceSimpleImpl;

public class MessageAppTest {

	Messenger messenger = new Messenger(new MessageServiceSimpleImpl());

	private final String VALID_SERVER = "inf.ug.edu.pl";
	private final String INVALID_SERVER = "inf.ug.edu.eu";

	private final String VALID_MESSAGE = "some message";
	private final String INVALID_MESSAGE = "ab";

	@Test
	public void checkSendingMessage() {

		assertEquals(1, messenger.sendMessage(INVALID_SERVER, VALID_MESSAGE));
		assertEquals(2, messenger.sendMessage(VALID_SERVER, INVALID_MESSAGE));

		assertThat(messenger.sendMessage(VALID_SERVER, VALID_MESSAGE),
				either(equalTo(0)).or(equalTo(1)));
	}

	@Test
    public void checkTestingConnection() {

        assertThat(messenger.testConnection(INVALID_SERVER), equalTo(1));
        assertThat(messenger.testConnection(VALID_SERVER), equalTo(0));
    }
}

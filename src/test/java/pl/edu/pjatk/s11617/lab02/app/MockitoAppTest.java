package pl.edu.pjatk.s11617.lab02.app;

import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.edu.pjatk.s11617.lab02.app.Messenger;
import pl.edu.pjatk.s11617.lab02.messenger.*;

@RunWith(MockitoJUnitRunner.class)
public class MockitoAppTest {

    private static final String VALID_SERVER = "inf.ug.edu.pl";
    private static final String INVALID_SERVER = "inf.ug.edu.eu";

    private static final String VALID_MESSAGE = "some message";
    private static final String INVALID_MESSAGE = "ab";

    @InjectMocks
    Messenger messenger = new Messenger(new MessageServiceSimpleImpl());

    @Mock
    MessageService msMock;

    @Test
    public void checkSendingMessage() throws MalformedRecipientException {

        when(msMock.send(INVALID_SERVER, VALID_MESSAGE)).thenReturn(SendingStatus.SENDING_ERROR);
        doThrow(new MalformedRecipientException()).when(msMock).send(VALID_SERVER, INVALID_MESSAGE);
        when(msMock.send(VALID_SERVER, VALID_MESSAGE)).thenReturn(SendingStatus.SENT);

        assertThat(messenger.sendMessage(INVALID_SERVER, VALID_MESSAGE), equalTo(1));
        assertThat(messenger.sendMessage(VALID_SERVER, INVALID_MESSAGE), equalTo(2));
        assertThat(messenger.sendMessage(VALID_SERVER, VALID_MESSAGE), either(equalTo(0)).or(equalTo(1)));

        verify(msMock, atMost(1)).send(INVALID_SERVER, VALID_MESSAGE);
        verify(msMock, atMost(1)).send(VALID_SERVER, INVALID_MESSAGE);
        verify(msMock, atMost(1)).send(VALID_SERVER, VALID_MESSAGE);
    }

    @Test
    public void checkTestingConnection() {

        when(msMock.checkConnection(VALID_SERVER)).thenReturn(ConnectionStatus.SUCCESS);
        when(msMock.checkConnection(INVALID_SERVER)).thenReturn(ConnectionStatus.FAILURE);

        assertThat(messenger.testConnection(VALID_SERVER), equalTo(0));
        assertThat(messenger.testConnection(INVALID_SERVER), equalTo(1));

        verify(msMock, times(1)).checkConnection(VALID_SERVER);
        verify(msMock, times(1)).checkConnection(INVALID_SERVER);
    }
}

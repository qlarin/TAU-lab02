package pl.edu.pjatk.s11617.lab02.app;

import org.jmock.*;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import pl.edu.pjatk.s11617.lab02.app.Messenger;
import pl.edu.pjatk.s11617.lab02.messenger.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(JMock.class)
public class JMockAppTest {

    private static final String VALID_SERVER = "inf.ug.edu.pl";
    private static final String INVALID_SERVER = "inf.ug.edu.eu";

    private static final String VALID_MESSAGE = "some message";
    private static final String INVALID_MESSAGE = "ab";

    //SUT
    private Messenger messenger;
    private MessageServiceSimpleImpl messengerMock;
    private Mockery context;

    @Before
    public void setUp() {
        context = new JUnit4Mockery() {{
            setImposteriser(ClassImposteriser.INSTANCE);
        }};
        messengerMock = context.mock(MessageServiceSimpleImpl.class);
        messenger = new Messenger(messengerMock);
    }

    @Test
    public void checkSendingMessage() throws MalformedRecipientException {

        context.checking(new Expectations() {{
            exactly(3).of(messengerMock).send(
                    with(anyOf(same(VALID_SERVER), same(INVALID_SERVER))),
                    with(anyOf(same(VALID_MESSAGE), same(INVALID_MESSAGE)))
            );
            will(onConsecutiveCalls(
                    returnValue(SendingStatus.SENDING_ERROR),
                    throwException(new MalformedRecipientException()),
                    returnValue(SendingStatus.SENT)
            ));
        }});

        assertThat(messenger.sendMessage(INVALID_SERVER, VALID_MESSAGE), equalTo(1));
        assertThat(messenger.sendMessage(VALID_SERVER, INVALID_MESSAGE), equalTo(2));
        assertThat(messenger.sendMessage(VALID_SERVER, VALID_MESSAGE), either(equalTo(0)).or(equalTo(1)));
        context.assertIsSatisfied();
    }

    @Test
    public void checkTestingConnection() {

        context.checking(new Expectations() {{
            exactly(2).of(messengerMock).checkConnection(with(anyOf(same(VALID_SERVER), same(INVALID_SERVER))));
            will(onConsecutiveCalls(
                    returnValue(ConnectionStatus.SUCCESS),
                    returnValue(ConnectionStatus.FAILURE)
            ));
        }});

        assertThat(messenger.testConnection(VALID_SERVER), equalTo(0));
        assertThat(messenger.testConnection(INVALID_SERVER), equalTo(1));
        context.assertIsSatisfied();
    }

}

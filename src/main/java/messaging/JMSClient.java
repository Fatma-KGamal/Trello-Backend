package messaging;

import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;

@Singleton
@Startup
public class JMSClient {

	@Resource(mappedName = "java:/jms/queue/DLQ")
	private Queue queue;
	@Inject
	JMSContext context;

	public void sendMessage(String message) {
		// send message to the queue

		try {
			JMSProducer producer = context.createProducer();
			TextMessage txtMessage = context.createTextMessage(message);
			producer.send(queue, message);

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

}

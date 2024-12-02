package idv.example.mq;

import com.ibm.mq.jms.MQQueueConnection;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.mq.jms.MQQueueSession;
import com.ibm.msg.client.wmq.WMQConstants;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

//MultiThreads, each thread is a listener, each listener listens to the same queue,
//and each listener will receive the message from the queue.
public class MqListener implements Runnable{
	// Queue Manager details
	private final String QUEUE_MANAGER="QM2";
	private final String QUEUE_NAME="DEV.QUEUE.1";
	private final String CHANNEL="DEV.APP.SVRCONN";
	private final String HOST_NAME="localhost";
	private final int PORT=1414;

	@Override
	public void run(){
		try{
			// Create connection factory
			MQQueueConnectionFactory connectionFactory=new MQQueueConnectionFactory();
			connectionFactory.setHostName(HOST_NAME); // MQ Server's hostname
			connectionFactory.setPort(PORT); // Port for listener
			connectionFactory.setChannel(CHANNEL); // MQ Channel name
			connectionFactory.setQueueManager(QUEUE_MANAGER); // Queue manager name
			connectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT); // Client connection mode
			// Create JMS connection
			MQQueueConnection connection=(MQQueueConnection)connectionFactory.createQueueConnection();
			connection.start(); // Start the connection
			// Create a session (non-transacted, auto acknowledgment)
			MQQueueSession session=(MQQueueSession)connection.createQueueSession(false,Session.AUTO_ACKNOWLEDGE);
			// Create the queue object using just the queue name
			Queue queue=session.createQueue(QUEUE_NAME);
			MessageConsumer consumer=session.createConsumer(queue);
			while(true){
				Message message=consumer.receive();  // This call blocks and waits for a message
				if(message!=null){
					if(message instanceof TextMessage){
						TextMessage textMessage=(TextMessage)message;
						System.out.println(" received Message text: "+textMessage.getText());
					}else if(message instanceof BytesMessage){
						BytesMessage byteMessage=(BytesMessage)message;
						byte[] data=new byte[(int)byteMessage.getBodyLength()];
						byteMessage.readBytes(data);
						System.out.println(" received Bytes Message byte: "+new String(data));
					}else{
						System.out.println("Received non-text message.");
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		List<Thread> listeners=new ArrayList<>();
		for(int i=0;i<10;i++){
			Thread listener=new Thread(new MqListener());
			listeners.add(listener);
		}
		listeners.forEach(Thread::start);
	}
}

package idv.example.mq;

import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;

public class MqConsumer{
	public static void main(String[] args) {
		// MQ connection details
		String host = "localhost";
		int port = 1414;
		String channel = "DEV.APP.SVRCONN";
		String queueManagerName = "QM1";
		String queueName = "DEV.QUEUE.1";

		try {
			// Create connection properties
			com.ibm.mq.MQEnvironment.hostname = host;
			com.ibm.mq.MQEnvironment.port = port;
			com.ibm.mq.MQEnvironment.channel = channel;

			// Connect to the queue manager
			MQQueueManager queueManager = new MQQueueManager(queueManagerName);

			// Open the queue for input
			MQQueue queue = queueManager.accessQueue(queueName, CMQC.MQOO_INPUT_AS_Q_DEF);

			// Receive a message
			MQMessage receiveMessage=new MQMessage();
			queue.get(receiveMessage);
			//convert message to string
			String message = receiveMessage.readStringOfByteLength(receiveMessage.getMessageLength());
			System.out.println("Message received: " + message);

			// Close queue and disconnect
			queue.close();
			queueManager.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

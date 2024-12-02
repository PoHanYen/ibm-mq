package idv.example.mq;

import com.ibm.mq.MQMessage;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;

public class MqProducer{
	public static void main(String[] args){
		// MQ connection details
		String host="localhost";
		int port=1414;
		String channel="DEV.APP.SVRCONN";
		String queueManagerName="QM2";
		String queueName="DEV.QUEUE.1";
		// Message to send
		String message="Hello, World!";
		try{
			// Create connection properties
			com.ibm.mq.MQEnvironment.hostname=host;
			com.ibm.mq.MQEnvironment.port=port;
			com.ibm.mq.MQEnvironment.channel=channel;
			// Connect to the queue manager
			MQQueueManager queueManager=new MQQueueManager(queueManagerName);
			// Open the queue for output
			MQQueue queue=queueManager.accessQueue(queueName,CMQC.MQOO_OUTPUT);
			// Create a message
			MQMessage sendMessage=new MQMessage();
			sendMessage.writeString(message);
			// Send the message
			queue.put(sendMessage);
			System.out.println("Message sent: "+message);
			// Close queue and disconnect
			queue.close();
			queueManager.disconnect();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

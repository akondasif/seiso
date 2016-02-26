package com.expedia.seiso.web.eventhandler;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import com.expedia.seiso.MQConnectionProperties;
import com.expedia.seiso.domain.entity.Node;
import com.expedia.seiso.domain.entity.ServiceInstance;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@EnableConfigurationProperties
public class RabbitMQSender {

	private MQConnectionProperties mqProps;
	
	private ConnectionFactory factory;
	
	private Channel channel;
	
	private Connection connection;

	public static final String NODE_UPDATE_MESSAGE = "TBD";

	public static final String SERVICE_INSTANCE_UPDATE_MESSAGE = "TBD";
	
	public static final String SI_CREATE_QUEUE_NAME = "serviceInstance.create";
	
	public static final String SI_UPDATE_QUEUE_NAME = "serviceInstance.update";
	
	public static final String SI_DELETE_QUEUE_NAME = "serviceInstance.delete";
	
	public static final String NODE_CREATE_QUEUE_NAME = "node.create";
	
	public static final String NODE_UPDATE_QUEUE_NAME = "node.update";
	
	public static final String NODE_DELETE_QUEUE_NAME = "node.delete";
	
	private String exchange = "";
	
	private boolean initialized = false;
	
	public RabbitMQSender() throws IOException {
		
	}
	
	public boolean nodeCreated(Node node){
		try {
			String message = "{\r\n  \"itemType\":\"Node\"\r\n  \"itemKey\":\"" + node.getId() + "\"\r\n  \"operation\":\"create\"}";
			return sendMessage(message, NODE_CREATE_QUEUE_NAME);
		}
		catch (Exception e){
			return false;
		}
	}
	
	private void init(){
		factory = new ConnectionFactory();
		try {
			
			this.factory.setHost(mqProps.getHost());
			this.factory.setPort(mqProps.getPort());
			this.factory.setUsername(mqProps.getUsername());
			this.factory.setPassword(mqProps.getPassword());
			this.exchange = "";
			
			this.connection = factory.newConnection();
			this.channel = connection.createChannel();
			// Declare the node change queues
			this.channel.queueDeclare(NODE_CREATE_QUEUE_NAME, false, false, false, null);
			this.channel.queueDeclare(NODE_UPDATE_QUEUE_NAME, false, false, false, null);
			this.channel.queueDeclare(NODE_DELETE_QUEUE_NAME, false, false, false, null);
			// Declare the service instance queues
			this.channel.queueDeclare(SI_CREATE_QUEUE_NAME, false, false, false, null);
			this.channel.queueDeclare(SI_UPDATE_QUEUE_NAME, false, false, false, null);
			this.channel.queueDeclare(SI_DELETE_QUEUE_NAME, false, false, false, null);
			this.initialized = true;
			
		} catch (IOException e) {
			log.error("Unable to initialize Rabbit MQ messaging", e);
		}
	}
	
	public boolean nodeUpdated(Node node){
		try {
			if (!this.initialized){
				this.init();
			}
			String message = "{\r\n  \"itemType\":\"Node\"\r\n  \"itemKey\":\"" + node.getId() + "\"\r\n  \"operation\":\"update\"}";
			return sendMessage(message, NODE_UPDATE_QUEUE_NAME);
		}
		catch (Exception e){
			return false;
		}
	}
	
	public boolean nodeDeleted(Node node){
		try {
			if (!this.initialized){
				this.init();
			}
			String message = "{\r\n  \"itemType\":\"Node\"\r\n  \"itemKey\":\"" + node.getId() + "\"\r\n  \"operation\":\"delete\"}";
			return sendMessage(message, NODE_DELETE_QUEUE_NAME);
		}
		catch (Exception e){
			return false;
		}
	}
	
	public boolean serviceInstanceCreated(ServiceInstance si){
		try {
			if (!this.initialized){
				this.init();
			}
			String message = "{\r\n  \"itemType\":\"ServiceInstance\"\r\n  \"itemKey\":\"" + si.getId() + "\"\r\n  \"operation\":\"create\"}";
			return sendMessage(message, SI_CREATE_QUEUE_NAME);
		}
		catch (Exception e){
			return false;
		}
	}
	
	public boolean serviceInstanceUpdated(ServiceInstance si){
		try {
			if (!this.initialized){
				this.init();
			}
			String message = "{\r\n  \"itemType\":\"ServiceInstance\"\r\n  \"itemKey\":\"" + si.getId() + "\"\r\n  \"operation\":\"update\"}";
			return sendMessage(message, SI_UPDATE_QUEUE_NAME);
		}
		catch (Exception e){
			return false;
		}
	}
	
	public boolean serviceInstanceDeleted(ServiceInstance si){
		try {
			if (!this.initialized){
				this.init();
			}
			String message = "{\r\n  \"itemType\":\"ServiceInstance\"\r\n  \"itemKey\":\"" + si.getId() + "\"\r\n  \"operation\":\"delete\"}";
			return sendMessage(message, SI_DELETE_QUEUE_NAME);
		}
		catch (Exception e){
			return false;
		}
	}
	
	private boolean sendMessage(String message, String queueName){
	    try {
			channel.basicPublish(this.exchange, queueName, null, message.getBytes());
			log.info("MQ message Sent :: QUEUE :: " + queueName + " :: '"  + message + "'");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean close(){
		try {
			channel.close();
			connection.close();
			log.info("MQ connection closed");
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	
	
}

package com.mom.activemq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Destination;


@RestController
public class TestController {
	@Autowired
	private JmsTemplate jmsTemplate;
    @Value("${activemq.queue.name}")
    private String queueName;
    @Value("${activemq.topic.name}")
    private String topicName;

    /**
     * 发送订阅
     */
    @GetMapping("/queue/send")
    public void updateItem(String message) {
    	jmsTemplate.convertAndSend(queueName, message);
    }

    /**
     * 发送消息destination是发送到的队列
     */
    public void sendMessage(Destination destination, String message) {
        jmsTemplate.convertAndSend(destination, message);
    }

    /**
     * 发送消息Topic
     */
    @GetMapping("/topic/send")
    public void sendMessage(String message) {
        jmsTemplate.convertAndSend(topicName, message);
    }
}
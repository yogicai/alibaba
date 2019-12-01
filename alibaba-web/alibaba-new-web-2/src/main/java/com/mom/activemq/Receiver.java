package com.mom.activemq;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


@Component
public class Receiver {

    @Value("${activemq.queue.name}")
    private String queueName;
    @Value("${activemq.topic.name}")
    private String topicName;


    @JmsListener(destination = "${activemq.queue.name}", containerFactory = "jmsListenerContainerQueue")
    public void receiveMessage(String message) {
        System.out.println("Received queue["+ queueName + "]<" + message + ">");
    }

    @JmsListener(destination = "${activemq.topic.name}", containerFactory = "jmsListenerContainerTopic")
    public void receiveTopicMessage(String message) {
        System.out.println("Received topic["+ topicName + "]<" + message + ">");
    }

}
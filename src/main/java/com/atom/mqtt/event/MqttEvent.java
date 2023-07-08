package com.atom.mqtt.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author Atom
 */
public class MqttEvent extends ApplicationEvent {

    private String topic;
    private String payload;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public MqttEvent(Object source, String topic, String payload) {
        super(source);
        this.topic = topic;
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "MqttEvent{" +
                "topic='" + topic + '\'' +
                ", payload='" + payload + '\'' +
                ", source=" + source +
                '}';
    }
}

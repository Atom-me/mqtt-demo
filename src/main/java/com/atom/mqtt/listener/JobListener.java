package com.atom.mqtt.listener;

import com.atom.mqtt.event.MqttEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Atom
 */
@Slf4j
@Component
public class JobListener {

    /**
     * 监听topic
     *
     * @param mqttEvent
     */
    @EventListener(condition = "#mqttEvent.topic.equals('pay')")
    public void onMqttCall1(MqttEvent mqttEvent) {
        log.info("接收到消111111111111111：{},{}", mqttEvent.getTopic(), mqttEvent.getPayload());

    }

    /**
     * 监听topic
     *
     * @param mqttEvent
     */
    @EventListener(condition = "#mqttEvent.topic.startsWith('device')")
    public void onMqttCallT(MqttEvent mqttEvent) {
        log.info("接收到消2222222222222：{},{}", mqttEvent.getTopic(), mqttEvent.getPayload());
    }

    /**
     * 监听topic
     *
     * @param mqttEvent
     */
    @EventListener(MqttEvent.class)
    public void onMqttCallTT(MqttEvent mqttEvent) {
        log.info("接收到消333333333333333：{}", mqttEvent.toString());
    }
}

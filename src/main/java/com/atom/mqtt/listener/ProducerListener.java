package com.atom.mqtt.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.integration.mqtt.event.MqttMessageDeliveredEvent;
import org.springframework.integration.mqtt.event.MqttMessageSentEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author Atom
 */
@Component
public class ProducerListener {

    private static final Logger log = LoggerFactory.getLogger(ProducerListener.class);


    /**
     * 消息发送之后触发此事件
     * 当async和async事件(async - events)都为true时, 将发出MqttMessageSentEvent
     * 它包含消息、主题、客户端库生成的消息id、clientId和clientInstance（每次连接客户端时递增）
     */
    @EventListener(MqttMessageSentEvent.class)
    public void mqttMessageSentEvent(MqttMessageSentEvent event) {
        log.info("mqttMessageSentEvent 发送信息: date={}, info={}", LocalDateTime.now(), event.toString());
    }

    /**
     * 消息发送成功之后触发此事件
     * 当async和async事件(async - events)都为true时, 将发出 MqttMessageDeliveredEvent
     * 当客户端库确认传递时，将发出 MqttMessageDeliveredEvent。它包含messageId、clientId和clientInstance，
     */
    @EventListener(MqttMessageDeliveredEvent.class)
    public void mqttMessageDeliveredEvent(MqttMessageDeliveredEvent event) {
        log.info("mqttMessageDeliveredEvent 发送成功信息: date={}, info={}", LocalDateTime.now(), event.toString());
    }

}

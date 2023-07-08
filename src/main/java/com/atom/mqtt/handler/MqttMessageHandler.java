package com.atom.mqtt.handler;

import com.atom.mqtt.config.MqttSubscriberConfig;
import com.atom.mqtt.event.MqttEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

/**
 * @author Atom
 */
@Component
@Slf4j
public class MqttMessageHandler implements MessageHandler {

    @Autowired
    private ApplicationEventPublisher eventPublisher;


    /**
     * ServiceActivator 注解表明当前方法用于处理MQTT消息，inputChannel 参数指定了用于接收消息的channel。
     *
     * @param message
     * @throws MessagingException
     */
    @Override
    @ServiceActivator(inputChannel = MqttSubscriberConfig.INPUT_CHANNEL)
    public void handleMessage(Message<?> message) throws MessagingException {
        String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();
        String qos = message.getHeaders().get(MqttHeaders.RECEIVED_QOS).toString();
        String payload = message.getPayload().toString();
        //////////////////处理订阅topic:(data/#)到的所有的数据
        log.info("接收到 mqtt消息，主题:{} 消息:{}", topic, payload);
        eventPublisher.publishEvent(new MqttEvent(this, topic, payload));
    }

}

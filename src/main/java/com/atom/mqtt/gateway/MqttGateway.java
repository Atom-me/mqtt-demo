package com.atom.mqtt.gateway;

import com.atom.mqtt.config.MqttProducerConfig;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * 消息网关，用来发送消息
 * <p>
 * MQTT 对外提供发送消息的API时，需要使用@MessagingGateway 注解，去提供一个消息网关代理，参数defaultRequestChannel 指定发送消息绑定的channel。
 * <p>
 * 可以实现三种API接口，payload 为发送的消息，topic 发送消息的主题，qos 消息质量。
 *
 * @author Atom
 */
@Component
@MessagingGateway(defaultRequestChannel = MqttProducerConfig.OUTBOUND_CHANNEL)
public interface MqttGateway {
    /**
     * 向默认的 topic 发送消息
     *
     * @param payload
     */
    void sendToMqtt(String payload);

    /**
     * 向指定的 topic 发送消息
     *
     * @param payload
     * @param topic
     */
    void sendToMqtt(String payload, @Header(MqttHeaders.TOPIC) String topic);

    /**
     * 向指定的 topic 发送消息，并指定服务质量参数
     *
     * @param topic
     * @param qos
     * @param payload
     */
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos, String payload);
}

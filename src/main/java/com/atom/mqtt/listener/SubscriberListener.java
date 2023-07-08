package com.atom.mqtt.listener;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.integration.mqtt.event.MqttConnectionFailedEvent;
import org.springframework.integration.mqtt.event.MqttSubscribedEvent;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author Atom
 */
@Component
public class SubscriberListener {

    private static final Logger log = LoggerFactory.getLogger(SubscriberListener.class);

    /**
     * 成功订阅到主题，MqttSubscribedEvent 事件就会被触发(多个主题,多次触发)
     */
    @EventListener(MqttSubscribedEvent.class)
    public void mqttSubscribedEvent(MqttSubscribedEvent event) {
        MqttPahoMessageDrivenChannelAdapter adapter = event.getSourceAsType();
        MqttConnectOptions connectionInfo = adapter.getConnectionInfo();
        String userName = connectionInfo.getUserName();
        String[] serverURIs = connectionInfo.getServerURIs();
        log.info("mqttSubscribedEvent 订阅成功信息: date={}, info={}", LocalDateTime.now(), event);
    }

    /**
     * mqtt任何客户端（producer/subscriber）连接失败或者订阅失败时, 都会触发 MqttConnectionFailedEvent 事件
     */
    @EventListener(MqttConnectionFailedEvent.class)
    public void mqttConnectionFailedEvent(MqttConnectionFailedEvent event) {
        log.error("mqttConnectionFailedEvent 连接mqtt失败: date={}, error={}", LocalDateTime.now(), event.toString());
    }
}

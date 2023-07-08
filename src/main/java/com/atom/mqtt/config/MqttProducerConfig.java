package com.atom.mqtt.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * @author Atom
 */
@Configuration
@EnableConfigurationProperties(value = {MqttProperties.class})
public class MqttProducerConfig {

    public static final String OUTBOUND_CHANNEL = "mqttOutboundChannel";

    /**
     * 构造一个默认的mqtt客户端操作bean
     *
     * @return
     */
    @Bean
    public MqttPahoClientFactory clientFactory(MqttProperties properties) {
        // MQTT的连接设置
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        // 设置连接的用户名
        mqttConnectOptions.setUserName(properties.getUserName());
        // 设置连接的密码
        mqttConnectOptions.setPassword(properties.getPassword().toCharArray());
        // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，
        // 把配置里的 cleanSession 设为false，客户端掉线后 服务器端不会清除session，
        // 当重连后可以接收之前订阅主题的消息。当客户端上线后会接受到它离线的这段时间的消息
        mqttConnectOptions.setCleanSession(true);
        // 设置发布端地址,多个用逗号分隔, 如:tcp://111:1883,tcp://222:1883
        // 当第一个111连接上后,222不会在连,如果111挂掉后,重试连111几次失败后,会自动去连接222
        mqttConnectOptions.setServerURIs(properties.getUris());
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
        mqttConnectOptions.setKeepAliveInterval(20);
        mqttConnectOptions.setAutomaticReconnect(true);
        // 设置“遗嘱”消息的话题，若客户端与服务器之间的连接意外中断，服务器将发布客户端的“遗嘱”消息。
        mqttConnectOptions.setWill(properties.getLastWillTopic(), properties.getLastWillMessage().getBytes(), 2, false);
        mqttConnectOptions.setMaxInflight(1000000);

        final DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(mqttConnectOptions);
        return factory;
    }

    /**
     * 发送通道
     *
     * @return
     */
    @Bean(value = OUTBOUND_CHANNEL)
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }


    /**
     * 发送消息和消费消息Channel可以使用相同MqttPahoClientFactory
     *
     * @param clientFactory
     * @param properties
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = OUTBOUND_CHANNEL)
    public MessageHandler mqttOutbound(MqttPahoClientFactory clientFactory, MqttProperties properties) {
        //clientId每个连接必须唯一,否则,两个相同的clientId相互挤掉线
//        String clientIdStr = properties.getClientId() + "-" + new SecureRandom().nextInt(100);
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(properties.getClientId(), clientFactory);
        //async如果为true，则调用方不会阻塞。而是在发送消息时等待传递确认。默认值为false（发送将阻塞，直到确认发送)
        messageHandler.setAsync(true);
        messageHandler.setAsyncEvents(true);
        messageHandler.setDefaultTopic(properties.getDefaultTopic());
        messageHandler.setDefaultQos(2);
        return messageHandler;
    }

}


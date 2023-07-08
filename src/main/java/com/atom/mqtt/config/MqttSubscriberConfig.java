package com.atom.mqtt.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;

/**
 * @author Atom
 */
@Configuration
@EnableConfigurationProperties(value = {MqttProperties.class})
public class MqttSubscriberConfig {

    private static final Logger log = LoggerFactory.getLogger(MqttSubscriberConfig.class);

    public static final String INPUT_CHANNEL = "mqttInputChannel";

    /**
     * 接收通道
     */
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }


    @Bean
    public MqttPahoClientFactory subscriberClientFactory(MqttProperties properties) {
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
        mqttConnectOptions.setKeepAliveInterval(60);
        //客户端掉线之后是否自动连接,默认true
        mqttConnectOptions.setAutomaticReconnect(true);
        // 设置“遗嘱”消息的话题，若客户端与服务器之间的连接意外中断，服务器将发布客户端的“遗嘱”消息。
        mqttConnectOptions.setWill(properties.getLastWillTopic(),
                properties.getLastWillMessage().getBytes(),
                properties.getLastWillQos(),
                properties.getLastWillRetain());

        mqttConnectOptions.setMaxInflight(1000000);

        final DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(mqttConnectOptions);
        return factory;
    }

    /**
     * MQTT接收消息通道适配器
     *
     * @param subscriberClientFactory
     * @param properties
     * @return
     */
    @Bean
    public MqttPahoMessageDrivenChannelAdapter adapter(MqttPahoClientFactory subscriberClientFactory, MqttProperties properties) {
//        String serverIdStr = properties.getServerId() + "-" + new SecureRandom().nextInt(100);
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(properties.getServerId(), subscriberClientFactory, properties.getDataTopics());
        return adapter;
    }

    /**
     * 配置client,监听的topic
     *
     * @return
     */
    @Bean
    public MessageProducer inbound(MqttPahoMessageDrivenChannelAdapter adapter, MqttProperties properties) {
        adapter.setCompletionTimeout(properties.getCompletionTimeout());
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }


}

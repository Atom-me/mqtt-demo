package com.atom.mqtt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Atom
 */
@Data
@ConfigurationProperties(value = "spring.mqtt")
public class MqttProperties {

    /**
     * mqtt Broker地址
     */
    private String[] uris;
    /**
     * 连接用户名
     */
    private String userName;
    /**
     * 连接密码
     */
    private String password;
    /**
     * 客户端ID
     */
    private String clientId;
    /**
     * 客户端ID
     */
    private String serverId;
    /**
     * 发送消息时，如果不指定topic，则使用此topic。
     * 发送topic不可包含#+模糊字符，必须指定一个明确的topic
     */
    private String defaultTopic;
    private String[] dataTopics;
    /**
     * 遗嘱主题，不可使用通配符
     */
    private String lastWillTopic;
    /**
     * 遗嘱QoS
     */
    private Integer lastWillQos;
    /**
     * 遗嘱消息
     */
    private String lastWillMessage;
    /**
     * 遗嘱保留,遗嘱保留用于设置遗嘱消息是否需要进行保留处理。服务端会根据此处内容，对遗嘱消息进行相应的保留与否处理。
     */
    private Boolean lastWillRetain;
    private int completionTimeout;


}


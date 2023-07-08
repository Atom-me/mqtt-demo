package com.atom.mqtt.controller;

import com.atom.mqtt.common.vo.ResponseMessage;
import com.atom.mqtt.gateway.MqttGateway;
import com.atom.mqtt.service.MqttService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Atom
 */
@RestController
@RequestMapping(value = "mqtt")
public class MqttController {

    @Autowired
    private MqttGateway mqttGateway;
    @Autowired
    private MqttService mqttService;


    /**
     * curl --location 'localhost:8080/mqtt/sendMessage?topic=device%2Fxxfd&message=%E6%B5%8B%E8%AF%95%E6%B6%88%E6%81%AF'
     * @param topic
     * @param message
     * @return
     */
    @GetMapping(value = "sendMessage")
    public ResponseMessage sendMessage(String topic, String message) {
        for (int i = 0; i < 5; i++) {
            if (Strings.isBlank(topic)) {
                mqttGateway.sendToMqtt(message);
            } else {
                mqttGateway.sendToMqtt(topic, 1, message);
            }
        }
        return ResponseMessage.success();
    }

    @GetMapping(value = "addSubscribeTopic")
    public ResponseMessage addSubscribeTopic(String subscribeTopic) {
        mqttService.addSubscribeTopic(subscribeTopic);
        return ResponseMessage.success();
    }

    @GetMapping(value = "removeSubscribeTopic")
    public ResponseMessage removeSubscribeTopic(String subscribeTopic) {
        mqttService.removeSubscribeTopic(subscribeTopic);
        return ResponseMessage.success();
    }
}


package com.atom.mqtt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author Atom
 */
@Service
public class MqttServiceImpl implements MqttService {

    @Autowired
    private MqttPahoMessageDrivenChannelAdapter adapter;

    @Override
    public void addSubscribeTopic(String subscribeTopic) {
        String[] topics = adapter.getTopic();
        if (!Arrays.asList(topics).contains(subscribeTopic)) {
            adapter.addTopic(subscribeTopic);
        }
    }

    @Override
    public void removeSubscribeTopic(String subscribeTopic) {
        adapter.removeTopic(subscribeTopic);
    }
}

package com.atom.mqtt.service;

/**
 * @author Atom
 */
public interface MqttService {

    void addSubscribeTopic(String subscribeTopic);

    void removeSubscribeTopic(String subscribeTopic);
}

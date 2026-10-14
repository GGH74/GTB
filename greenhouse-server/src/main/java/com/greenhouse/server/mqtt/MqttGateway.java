package com.greenhouse.server.mqtt;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 * MQTT 消息发布网关
 * 使用方式：注入 MqttGateway 调用 send() 方法发布消息
 */
@MessagingGateway(defaultRequestChannel = "mqttOutputChannel")
public interface MqttGateway {

    /**
     * 发送消息到默认主题
     *
     * @param data 消息内容
     */
    void send(String data);

    /**
     * 发送消息到指定主题
     *
     * @param topic 主题
     * @param data  消息内容
     */
    void send(@Header(MqttHeaders.TOPIC) String topic, String data);

    /**
     * 发送消息到指定主题（指定 QoS）
     *
     * @param topic 主题
     * @param qos   QoS 等级
     * @param data  消息内容
     */
    void send(@Header(MqttHeaders.TOPIC) String topic,
              @Header(MqttHeaders.QOS) int qos,
              String data);
}

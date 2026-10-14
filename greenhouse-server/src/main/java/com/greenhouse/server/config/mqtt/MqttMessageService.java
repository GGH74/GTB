package com.greenhouse.server.config.mqtt;

import lombok.extern.slf4j.Slf4j;

/**
 * MQTT 消息处理服务
 * 接收并处理订阅的 MQTT 消息
 */
@Slf4j
public class MqttMessageService {

    /**
     * 处理收到的 MQTT 消息
     *
     * @param topic   主题
     * @param payload 消息内容
     */
    public void handleMessage(String topic, String payload) {
        log.info("收到 MQTT 消息 - topic: {}, payload: {}", topic, payload);

        try {
            // 根据主题分发处理
            if (topic.startsWith("greenhouse/sensor/data")) {
                handleSensorData(payload);
            } else if (topic.startsWith("greenhouse/device/status")) {
                handleDeviceStatus(payload);
            } else {
                log.warn("未处理的 MQTT 主题: {}", topic);
            }
        } catch (Exception e) {
            log.error("处理 MQTT 消息异常 - topic: {}, payload: {}", topic, payload, e);
        }
    }

    /**
     * 处理传感器数据消息
     */
    private void handleSensorData(String payload) {
        // TODO: 解析传感器数据并写入 InfluxDB
        log.debug("传感器数据: {}", payload);
    }

    /**
     * 处理设备状态消息
     */
    private void handleDeviceStatus(String payload) {
        // TODO: 更新设备在线状态
        log.debug("设备状态: {}", payload);
    }
}

package com.greenhouse.server.config.mqtt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * MQTT 配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "mqtt")
public class MqttProperties {

    /**
     * Broker 地址
     */
    private String brokerUrl;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 默认订阅主题
     */
    private String defaultTopic;

    /**
     * QoS 等级
     */
    private Integer qos = 1;

    /**
     * 连接超时（秒）
     */
    private Integer connectionTimeout = 10;

    /**
     * 心跳间隔（秒）
     */
    private Integer keepAliveInterval = 60;

    /**
     * 是否自动重连
     */
    private Boolean automaticReconnect = true;

    /**
     * 清除会话
     */
    private Boolean cleanSession = true;
}

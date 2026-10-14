package com.greenhouse.server.config.mqtt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import jakarta.annotation.Resource;

/**
 * MQTT 配置
 */
@Configuration
public class MqttConfig {

    @Resource
    private MqttProperties mqttProperties;

    /**
     * MQTT 客户端工厂
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        org.eclipse.paho.client.mqttv3.MqttConnectOptions options = new org.eclipse.paho.client.mqttv3.MqttConnectOptions();
        options.setServerURIs(new String[]{mqttProperties.getBrokerUrl()});
        options.setUserName(mqttProperties.getUsername());
        options.setPassword(mqttProperties.getPassword().toCharArray());
        options.setConnectionTimeout(mqttProperties.getConnectionTimeout());
        options.setKeepAliveInterval(mqttProperties.getKeepAliveInterval());
        options.setAutomaticReconnect(mqttProperties.getAutomaticReconnect());
        options.setCleanSession(mqttProperties.getCleanSession());
        factory.setConnectionOptions(options);
        return factory;
    }

    // ============================================================
    // 入站（订阅消息）
    // ============================================================

    /**
     * 入站通道
     */
    @Bean("mqttInputChannel")
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    /**
     * 入站适配器（订阅消息）
     */
    @Bean
    public MessageProducer mqttInbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(
                        mqttProperties.getClientId() + "_inbound",
                        mqttClientFactory(),
                        mqttProperties.getDefaultTopic()
                );
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(mqttProperties.getQos());
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    /**
     * 入站消息处理器（接收订阅的消息）
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler mqttMessageHandler() {
        return message -> {
            String topic = message.getHeaders().get("mqtt_receivedTopic", String.class);
            String payload = message.getPayload().toString();
            // 具体业务处理在 MqttMessageService 中
            mqttMessageService().handleMessage(topic, payload);
        };
    }

    // ============================================================
    // 出站（发布消息）
    // ============================================================

    /**
     * 出站通道
     */
    @Bean("mqttOutputChannel")
    public MessageChannel mqttOutputChannel() {
        return new DirectChannel();
    }

    /**
     * 出站处理器（发布消息）
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttOutputChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler handler = new MqttPahoMessageHandler(
                mqttProperties.getClientId() + "_outbound",
                mqttClientFactory()
        );
        handler.setAsync(true);
        handler.setDefaultQos(mqttProperties.getQos());
        handler.setDefaultTopic(mqttProperties.getDefaultTopic());
        return handler;
    }

    /**
     * 消息服务（手动注入避免循环依赖）
     */
    @Bean
    public MqttMessageService mqttMessageService() {
        return new MqttMessageService();
    }
}

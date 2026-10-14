package com.greenhouse.server.config.influxdb;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.Resource;

/**
 * InfluxDB 配置
 */
@Configuration
public class InfluxDbConfig {

    @Resource
    private InfluxDbProperties influxDbProperties;

    /**
     * InfluxDB 客户端
     */
    @Bean
    public InfluxDBClient influxDBClient() {
        InfluxDBClient client = InfluxDBClientFactory.create(
                influxDbProperties.getUrl(),
                influxDbProperties.getToken().toCharArray(),
                influxDbProperties.getOrg(),
                influxDbProperties.getBucket()
        );
        return client;
    }
}

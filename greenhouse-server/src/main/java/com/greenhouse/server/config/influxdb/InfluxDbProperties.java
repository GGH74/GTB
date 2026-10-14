package com.greenhouse.server.config.influxdb;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * InfluxDB 配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "influxdb")
public class InfluxDbProperties {

    /**
     * InfluxDB 地址
     */
    private String url;

    /**
     * 访问令牌
     */
    private String token;

    /**
     * 组织名称
     */
    private String org;

    /**
     * 默认存储桶
     */
    private String bucket;
}

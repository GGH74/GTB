package com.greenhouse.server.service;

import com.greenhouse.server.config.influxdb.InfluxDbProperties;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * InfluxDB 服务
 * 封装传感器时序数据的读写操作
 */
@Slf4j
@Service
public class InfluxDbService {

    @Resource
    private InfluxDBClient influxDBClient;

    @Resource
    private InfluxDbProperties influxDbProperties;

    /**
     * 写入单个传感器数据点
     *
     * @param measurement  测量名（如 temperature、humidity）
     * @param tags         标签（如 greenhouseId、sensorId）
     * @param fields       字段（传感器数值）
     * @param timestamp    时间戳（毫秒）
     */
    public void writePoint(String measurement, Map<String, String> tags,
                           Map<String, Object> fields, long timestamp) {
        try {
            Point point = Point.measurement(measurement)
                    .addTags(tags)
                    .addFields(convertFields(fields))
                    .time(Instant.ofEpochMilli(timestamp), WritePrecision.MS);

            try (WriteApi writeApi = influxDBClient.getWriteApi()) {
                writeApi.writePoint(point);
            }
            log.debug("写入 InfluxDB 成功 - measurement: {}, tags: {}", measurement, tags);
        } catch (Exception e) {
            log.error("写入 InfluxDB 失败 - measurement: {}", measurement, e);
        }
    }

    /**
     * 写入传感器数据（使用默认 bucket）
     *
     * @param measurement 测量名
     * @param tags        标签
     * @param fieldName   字段名
     * @param value       字段值
     */
    public void write(String measurement, Map<String, String> tags,
                      String fieldName, Number value) {
        Map<String, Object> fields = new HashMap<>();
        fields.put(fieldName, value);
        writePoint(measurement, tags, fields, System.currentTimeMillis());
    }

    /**
     * 查询数据（Flux 查询语句）
     *
     * @param flux Flux 查询语句
     * @return 记录列表
     */
    public List<FluxRecord> query(String flux) {
        try {
            List<FluxTable> tables = influxDBClient.getQueryApi().query(flux);
            List<FluxRecord> records = new ArrayList<>();
            for (FluxTable table : tables) {
                records.addAll(table.getRecords());
            }
            return records;
        } catch (Exception e) {
            log.error("查询 InfluxDB 失败 - flux: {}", flux, e);
            return new ArrayList<>();
        }
    }

    /**
     * 按时间范围查询传感器数据
     *
     * @param measurement  测量名
     * @param greenhouseId 大棚ID
     * @param sensorId     传感器ID（可选，null 则查全部）
     * @param startMs      开始时间（毫秒）
     * @param endMs        结束时间（毫秒）
     * @return 记录列表
     */
    public List<FluxRecord> queryByRange(String measurement, Long greenhouseId,
                                          Long sensorId, long startMs, long endMs) {
        StringBuilder flux = new StringBuilder();
        flux.append("from(bucket: \"").append(influxDbProperties.getBucket()).append("\")\n")
            .append("  |> range(start: ").append(Instant.ofEpochMilli(startMs))
            .append(", stop: ").append(Instant.ofEpochMilli(endMs)).append(")\n")
            .append("  |> filter(fn: (r) => r._measurement == \"").append(measurement).append("\")\n")
            .append("  |> filter(fn: (r) => r.greenhouseId == \"").append(greenhouseId).append("\")\n");

        if (sensorId != null) {
            flux.append("  |> filter(fn: (r) => r.sensorId == \"").append(sensorId).append("\")\n");
        }

        flux.append("  |> sort(columns: [\"_time\"])\n");

        return query(flux.toString());
    }

    /**
     * 查询最新的一个数据点
     *
     * @param measurement  测量名
     * @param greenhouseId 大棚ID
     * @param sensorId     传感器ID
     * @return 最新记录，没有则返回 null
     */
    public FluxRecord queryLatest(String measurement, Long greenhouseId, Long sensorId) {
        StringBuilder flux = new StringBuilder();
        flux.append("from(bucket: \"").append(influxDbProperties.getBucket()).append("\")\n")
            .append("  |> range(start: -1h)\n")
            .append("  |> filter(fn: (r) => r._measurement == \"").append(measurement).append("\")\n")
            .append("  |> filter(fn: (r) => r.greenhouseId == \"").append(greenhouseId).append("\")\n")
            .append("  |> filter(fn: (r) => r.sensorId == \"").append(sensorId).append("\")\n")
            .append("  |> sort(columns: [\"_time\"], desc: true)\n")
            .append("  |> limit(n: 1)\n");

        List<FluxRecord> records = query(flux.toString());
        return records.isEmpty() ? null : records.get(0);
    }

    /**
     * 字段值类型转换（InfluxDB 的 addFields 需要 Number/String/Boolean）
     */
    private Map<String, Object> convertFields(Map<String, Object> fields) {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Number || value instanceof String || value instanceof Boolean) {
                result.put(entry.getKey(), value);
            } else {
                result.put(entry.getKey(), value.toString());
            }
        }
        return result;
    }
}

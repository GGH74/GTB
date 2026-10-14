package com.greenhouse.server.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 结果状态码枚举
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    // ========== 通用 ==========
    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "没有权限访问"),
    NOT_FOUND(404, "请求资源不存在"),

    // ========== 用户相关 1xxx ==========
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_PASSWORD_ERROR(1002, "密码错误"),
    USER_DISABLED(1003, "账号已被禁用"),
    USER_USERNAME_EXIST(1004, "用户名已存在"),
    USER_PHONE_EXIST(1005, "手机号已注册"),

    // ========== 业务相关 2xxx ==========
    GREENHOUSE_NOT_FOUND(2001, "大棚不存在"),
    SENSOR_NOT_FOUND(2002, "传感器不存在"),
    ALERT_NOT_FOUND(2003, "告警记录不存在"),
    RULE_NOT_FOUND(2004, "规则不存在"),

    // ========== 系统相关 5xxx ==========
    SYSTEM_ERROR(5000, "系统异常"),
    PARAM_VALID_ERROR(5001, "参数校验失败"),
    FILE_UPLOAD_ERROR(5002, "文件上传失败"),
    ;

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 提示信息
     */
    private final String message;
}

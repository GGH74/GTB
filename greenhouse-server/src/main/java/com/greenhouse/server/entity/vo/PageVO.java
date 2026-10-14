package com.greenhouse.server.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果 VO
 *
 * @param <T> 数据类型
 */
@Data
public class PageVO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据列表
     */
    private List<T> records;

    /**
     * 总条数
     */
    private Long total;

    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 每页大小
     */
    private Integer pageSize;

    /**
     * 总页数
     */
    private Integer pages;
}

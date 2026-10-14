package com.greenhouse.server.common.constants;

/**
 * 通用常量
 */
public interface CommonConstants {

    /**
     * 逻辑删除：未删除
     */
    Integer NOT_DELETED = 0;

    /**
     * 逻辑删除：已删除
     */
    Integer DELETED = 1;

    /**
     * 状态：正常
     */
    Integer STATUS_NORMAL = 1;

    /**
     * 状态：禁用
     */
    Integer STATUS_DISABLED = 0;

    /**
     * 排序：升序
     */
    String SORT_ASC = "asc";

    /**
     * 排序：降序
     */
    String SORT_DESC = "desc";

    /**
     * 默认页码
     */
    Integer DEFAULT_PAGE_NUM = 1;

    /**
     * 默认每页大小
     */
    Integer DEFAULT_PAGE_SIZE = 10;

    /**
     * 最大每页大小
     */
    Integer MAX_PAGE_SIZE = 100;
}

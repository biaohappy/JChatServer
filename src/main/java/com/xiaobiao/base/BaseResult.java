package com.xiaobiao.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wuxiaobiao
 * @create 2017-12-14 11:25
 * To change this template use File | Settings | Editor | File and Code Templates.
 **/
@Getter
@Setter
public class BaseResult implements Serializable {

    private static final long serialVersionUID = -3480281349914507512L;
    /**
     * 是否在查询时没有查到记录时抛异常，默认为true（查询选填）
     */
    protected Boolean isNullError = true;

    /**
     * 分页当前页数（从1开始）
     */
    protected Integer pageCurrent;

    /**
     * 分页每页个数
     */
    protected Integer pageSize;

    /**
     * 排序属性
     */
    protected String orderField;

    /**
     * 排序规则
     */
    protected String orderRule;

    /**
     * 是否下载
     */
    protected Boolean isDownload = false;
}

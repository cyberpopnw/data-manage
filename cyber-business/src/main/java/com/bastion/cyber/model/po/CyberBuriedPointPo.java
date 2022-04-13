package com.bastion.cyber.model.po;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@ApiModel(value = "cyber_business.cyber_buried_point")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "cyber_business.cyber_buried_point")
public class CyberBuriedPointPo {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "")
    private Long id;

    /**
     * 用户Id
     */
    @TableField(value = "`uid`")
    @ApiModelProperty(value = "用户Id")
    private Long uid;

    /**
     * 请求ip地址
     */
    @TableField(value = "ip")
    @ApiModelProperty(value = "请求ip地址")
    private String ip;

    /**
     * 请求url
     */
    @TableField(value = "url")
    @ApiModelProperty(value = "请求url")
    private String url;

    /**
     * 请求方式
     */
    @TableField(value = "`method`")
    @ApiModelProperty(value = "请求方式")
    private String method;

    /**
     * 请求参数
     */
    @TableField(value = "params")
    @ApiModelProperty(value = "请求参数")
    private String params;

    /**
     * Ip来源
     */
    @TableField(value = "ip_source")
    @ApiModelProperty(value = "Ip来源")
    private String ipSource;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    public static final String COL_ID = "id";

    public static final String COL_UID = "uid";

    public static final String COL_IP = "ip";

    public static final String COL_URL = "url";

    public static final String COL_METHOD = "method";

    public static final String COL_PARAMS = "params";

    public static final String COL_IP_SOURCE = "ip_source";

    public static final String COL_CREATE_TIME = "create_time";

}
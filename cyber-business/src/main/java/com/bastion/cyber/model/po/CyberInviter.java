package com.bastion.cyber.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@ApiModel(value = "cyber_business.cyber_inviter")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "cyber_business.cyber_inviter")
public class CyberInviter extends BasePo{
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
     * 邀请码
     */
    @TableField(value = "inviter_code")
    @ApiModelProperty(value = "邀请码")
    private String inviterCode;

    /**
     * 邀请码
     */
    @TableField(value = "`level`")
    @ApiModelProperty(value = "邀请码")
    private Byte level;

    public static final String COL_ID = "id";
    public static final String COL_UID = "uid";
    public static final String COL_INVITER_CODE = "inviter_code";
    public static final String COL_LEVEL = "level";
    public static final String COL_CREATE_TIME = "create_time";
    public static final String COL_UPDATE_TIME = "update_time";
}
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

@ApiModel(value = "`cyber_user`")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName(value = "`cyber_user`")
public class UserPo extends BasePo {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "")
    private Long id;

    /**
     * 钱包地址
     */
    @TableField(value = "addr")
    @ApiModelProperty(value = "钱包地址")
    private String addr;

    /**
     * 用户昵称
     */
    @TableField(value = "`name`")
    @ApiModelProperty(value = "用户昵称")
    private String name;
    private String nonce;

    /**
     * 状态 0:封禁 1:正常
     */
    @TableField(value = "`status`")
    @ApiModelProperty(value = "状态 0:封禁 1:正常")
    private Boolean status;
    private Byte role;
    @TableField(value = "`inviter_id`")
    private Long inviterId;

    public static final String COL_ID = "id";

    public static final String COL_ADDR = "addr";

    public static final String COL_NAME = "name";

    public static final String COL_STATUS = "status";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_INVSTRING = "inviter_id";
}
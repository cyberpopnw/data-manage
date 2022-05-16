package cyber.dealer.sys.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 
 * @TableName cyber_users_record
 */
@TableName(value ="cyber_users_record")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CyberUsersRecord implements Serializable {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 操作
     */
    private String action;

    /**
     * 钱包
     */
    private String address;

    /**
     * 时间
     */
    private Date time;

    /**
     * 自定义参数
     */
    private String parameter1;

    /**
     * 
     */
    private String parameter2;

    /**
     * 
     */
    private String parameter3;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
package cyber.dealer.sys.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * 
 * @TableName cyber_users
 */
@TableName(value ="cyber_users")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CyberUsers implements Serializable {
    /**
     * 主键|邀请人id
     */
    @TableId
    private Long id;

    /**
     * 被邀请id
     */
    private Long invId;

    /**
     * 地址
     */
    private String address;

    /**
     * 用户等级
     */
    private Integer level;

    /**
     * 币总数
     */
    private Long fujiCoin;

    /**
     * 币总数
     */
    private Long mubaiCoin;

    /**
     * 是否下载
     */
    private Integer download;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
    private String email;

    private String nikename;

    private Long dobadge;

    private String personalrewards;

    private Long playgametimes;
    private String password;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
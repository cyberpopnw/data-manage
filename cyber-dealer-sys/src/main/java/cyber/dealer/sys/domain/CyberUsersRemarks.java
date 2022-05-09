package cyber.dealer.sys.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName cyber_users_remarks
 */
@TableName(value ="cyber_users_remarks")
@Data
public class CyberUsersRemarks implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private String toaddress;

    /**
     * 
     */
    private String address;

    /**
     * 
     */
    private String remarks;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
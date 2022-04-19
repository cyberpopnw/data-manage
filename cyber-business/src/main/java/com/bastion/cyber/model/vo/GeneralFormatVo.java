package com.bastion.cyber.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * @author lfy
 * @Date 2022/4/19 14:17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeneralFormatVo {

    public static final String GF_ACTION = "action";
    public static final String GF_ADDRESS = "address";
    public static final String GF_TIME = "time";
    public static final String GF_PARAMETER1 = "parameter1";
    public static final String GF_PARAMETER2 = "parameter2";
    public static final String GF_PARAMETER3 = "parameter3";
    //动作名称
    public String action;
    //钱包地址
    public String address;
    //时间戳
    public String time;
    //自定义参数
    public String parameter1;
    public String parameter2;
    public String parameter3;
}

package com.bastion.cyber.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created on 2022/4/7.
 *
 * @author zyg
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreLoginUserVo {
    private Long id;
    private String publicAddress;
    private String nonce;
    private String username;
}

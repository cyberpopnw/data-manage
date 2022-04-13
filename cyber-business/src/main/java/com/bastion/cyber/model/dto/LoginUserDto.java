package com.bastion.cyber.model.dto;

import com.bastion.cyber.model.vo.VoObject;
import lombok.Data;

/**
 * Created on 2022/4/7.
 *
 * @author zyg
 */
@Data
public class LoginUserDto implements VoObject {
    private String publicAddress;
    private String signature;
    private String nonce;
    private String inviterCode;

    @Override
    public Object createVo() {
        return null;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}

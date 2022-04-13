package com.bastion.cyber.model.bo;

import com.bastion.cyber.model.vo.VoObject;
import com.bastion.cyber.utils.UnSafeUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthBo implements VoObject {
    private Long id;

    @Override
    public Object createVo() {
        return null;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}

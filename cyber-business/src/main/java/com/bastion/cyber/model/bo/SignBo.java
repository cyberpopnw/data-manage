package com.bastion.cyber.model.bo;

import com.bastion.cyber.model.vo.VoObject;
import com.bastion.cyber.utils.UnSafeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2022/4/8.
 *
 * @author zyg
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignBo implements VoObject {
    private Long id;
    private String publicAddress;

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("publicAddress", publicAddress);
        return map;
    }

    @Override
    public Object createVo() {
        return null;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}

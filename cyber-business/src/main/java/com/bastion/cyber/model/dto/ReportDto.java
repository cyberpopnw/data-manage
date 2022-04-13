package com.bastion.cyber.model.dto;

import com.bastion.cyber.model.vo.VoObject;
import lombok.Data;

@Data
public class ReportDto implements VoObject {
    private String type;
    private String addr;
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

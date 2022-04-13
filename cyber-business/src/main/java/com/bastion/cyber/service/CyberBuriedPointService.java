package com.bastion.cyber.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bastion.cyber.constant.ReturnObject;
import com.bastion.cyber.model.po.CyberBuriedPointPo;

public interface CyberBuriedPointService extends IService<CyberBuriedPointPo> {

    ReturnObject<Object> insert(CyberBuriedPointPo cyberBuriedPointPo);
}

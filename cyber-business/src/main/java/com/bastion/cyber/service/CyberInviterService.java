package com.bastion.cyber.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bastion.cyber.constant.ReturnObject;
import com.bastion.cyber.model.bo.InviterBo;
import com.bastion.cyber.model.po.CyberInviter;

public interface CyberInviterService extends IService<CyberInviter> {

    ReturnObject<Object> create(Long uid, Byte level);

    ReturnObject<Object> findBy(InviterBo inviterBo);

    CyberInviter getuid(Long getid);
}

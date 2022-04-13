package com.bastion.cyber.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bastion.cyber.constant.ReturnObject;
import com.bastion.cyber.dao.CyberInviterDao;
import com.bastion.cyber.mapper.CyberInviterMapper;
import com.bastion.cyber.model.bo.InviterBo;
import com.bastion.cyber.model.po.CyberInviter;
import com.bastion.cyber.service.CyberInviterService;
import org.springframework.stereotype.Service;

import static com.bastion.cyber.utils.Common.getRandomString;

@Service
public class CyberInviterServiceImpl extends ServiceImpl<CyberInviterMapper, CyberInviter> implements CyberInviterService {
    private final CyberInviterDao cyberInviterDao;

    public CyberInviterServiceImpl(CyberInviterDao cyberInviterDao) {
        this.cyberInviterDao = cyberInviterDao;
    }

    @Override
    public ReturnObject<Object> create(Long uid, Byte level) {
        String inviterCode = getRandomString(6);
        return cyberInviterDao.create(uid, inviterCode, level);
    }

    @Override
    public ReturnObject<Object> findBy(InviterBo inviterBo) {
        return cyberInviterDao.findBy(inviterBo);
    }
}

package com.bastion.cyber.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bastion.cyber.constant.ReturnObject;
import com.bastion.cyber.dao.CyberBuriedPointDao;
import com.bastion.cyber.mapper.CyberBuriedPointPoMapper;
import com.bastion.cyber.model.po.CyberBuriedPointPo;
import com.bastion.cyber.service.CyberBuriedPointService;
import org.springframework.stereotype.Service;

@Service
public class CyberBuriedPointServiceImpl extends ServiceImpl<CyberBuriedPointPoMapper, CyberBuriedPointPo> implements CyberBuriedPointService {
    private final CyberBuriedPointDao cyberBuriedPointDao;

    public CyberBuriedPointServiceImpl(CyberBuriedPointDao cyberBuriedPointDao) {
        this.cyberBuriedPointDao = cyberBuriedPointDao;
    }

    @Override
    public ReturnObject<Object> insert(CyberBuriedPointPo cyberBuriedPointPo) {
        return cyberBuriedPointDao.insert(cyberBuriedPointPo);
    }
}

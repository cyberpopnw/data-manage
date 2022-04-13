package com.bastion.cyber.dao;

import com.bastion.cyber.constant.ReturnObject;
import com.bastion.cyber.mapper.CyberBuriedPointPoMapper;
import com.bastion.cyber.model.po.CyberBuriedPointPo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.bastion.cyber.constant.ReturnNo.INTERNAL_SERVER_ERR;

@Repository
public class CyberBuriedPointDao {
    private final CyberBuriedPointPoMapper cyberBuriedPointPoMapper;

    public CyberBuriedPointDao(CyberBuriedPointPoMapper cyberBuriedPointPoMapper) {
        this.cyberBuriedPointPoMapper = cyberBuriedPointPoMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public ReturnObject<Object> insert(CyberBuriedPointPo cyberBuriedPointPo) {
        try {
            return new ReturnObject<>(cyberBuriedPointPoMapper.insert(cyberBuriedPointPo));
        } catch (Exception e) {
            return new ReturnObject<>(INTERNAL_SERVER_ERR, e);
        }
    }
}

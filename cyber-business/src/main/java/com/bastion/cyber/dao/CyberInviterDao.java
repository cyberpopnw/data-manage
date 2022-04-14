package com.bastion.cyber.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bastion.cyber.constant.ReturnObject;
import com.bastion.cyber.mapper.CyberInviterMapper;
import com.bastion.cyber.model.bo.InviterBo;
import com.bastion.cyber.model.po.CyberInviter;
import com.bastion.cyber.model.po.UserPo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import static com.bastion.cyber.constant.ReturnNo.INTERNAL_SERVER_ERR;

@Repository
public class CyberInviterDao {
    private final CyberInviterMapper cyberInviterMapper;

    public CyberInviterDao(CyberInviterMapper cyberInviterMapper) {
        this.cyberInviterMapper = cyberInviterMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public ReturnObject<Object> create(Long uid, String inviterCode, Byte level) {

        CyberInviter cyberInviter = CyberInviter.builder().uid(uid).level(level).inviterCode(inviterCode).build();
        try {
            return new ReturnObject<>(cyberInviterMapper.insert(cyberInviter) > 0 ? cyberInviter : UserPo.builder());
        } catch (Exception e) {
            return new ReturnObject<>(INTERNAL_SERVER_ERR, e);
        }
    }

    @Transactional(readOnly = true)
    public ReturnObject<Object> findBy(InviterBo inviterBo) {
        String inviterCode = inviterBo.getInviterCode();
        LambdaQueryWrapper<CyberInviter> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(!StringUtils.isEmpty(inviterCode), CyberInviter::getInviterCode, inviterCode);
        try {
            return new ReturnObject<>(cyberInviterMapper.selectOne(wrapper));
        } catch (Exception e) {
            return new ReturnObject<>(INTERNAL_SERVER_ERR, e);
        }
    }

    public CyberInviter getuid(Long getid) {
        QueryWrapper<CyberInviter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(CyberInviter.COL_UID, getid);
        return  cyberInviterMapper.selectOne(queryWrapper);
    }
}

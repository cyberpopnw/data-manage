package com.bastion.cyber.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bastion.cyber.mapper.UserMapper;
import com.bastion.cyber.constant.ReturnObject;
import com.bastion.cyber.model.dto.UserDto;
import com.bastion.cyber.model.po.UserPo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import static com.bastion.cyber.constant.ReturnNo.INTERNAL_SERVER_ERR;

@Repository
public class UserDao {
    private final UserMapper userMapper;

    public UserDao(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Transactional(readOnly = true)
    public ReturnObject<Object> findBy(UserDto userDto) {
        Long userId = userDto.getId();
        String addr = userDto.getAddr();
        LambdaQueryWrapper<UserPo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(!StringUtils.isEmpty(userId), UserPo::getId, userId)
                .eq(!StringUtils.isEmpty(addr), UserPo::getAddr, addr);
        try {
            return new ReturnObject<>(userMapper.selectOne(wrapper));
        } catch (Exception e) {
            return new ReturnObject<>(INTERNAL_SERVER_ERR, e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ReturnObject<Object> updateBy(UserDto userDto) {
        Long id = userDto.getId();
        String name = userDto.getName();
        String nonce = userDto.getNonce();
        Long inviterId = userDto.getInviterId();
        LambdaUpdateWrapper<UserPo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserPo::getId, id)
                .set(!StringUtils.isEmpty(name), UserPo::getName, name)
                .set(!StringUtils.isEmpty(nonce), UserPo::getNonce, nonce)
                .set(!StringUtils.isEmpty(inviterId), UserPo::getInviterId, inviterId);
        try {
            return new ReturnObject<>(userMapper.update(UserPo.builder().build(), wrapper));
        } catch (Exception e) {
            return new ReturnObject<>(INTERNAL_SERVER_ERR, e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ReturnObject<Object> create(UserPo userPo) {
        try {
            return new ReturnObject<>(userMapper.insert(userPo) > 0 ? userPo : UserPo.builder());
        } catch (Exception e) {
            return new ReturnObject<>(INTERNAL_SERVER_ERR, e);
        }
    }
}

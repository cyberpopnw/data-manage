package com.bastion.cyber.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bastion.cyber.constant.ReturnObject;
import com.bastion.cyber.mapper.UserMapper;
import com.bastion.cyber.model.dto.UserDto;
import com.bastion.cyber.model.po.CyberInviter;
import com.bastion.cyber.model.po.UserPo;
import com.bastion.cyber.utils.HttpURLConnectionUtil;
import com.bastion.cyber.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bastion.cyber.constant.ReturnNo.INTERNAL_SERVER_ERR;

@Repository
public class UserDao {
    private final static List list = new ArrayList<String>() {
        {
            add("connectWallet");
            add("loginGame");
            add("buyBox");
        }
    };
    private final UserMapper userMapper;
    @Autowired
    private RedisUtils redisUtils;

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

    public ReturnObject<Object> getboolean(String addr) {
        QueryWrapper<UserPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(UserPo.COL_ADDR, addr)
                .eq("role", 2);
        UserPo userPo = null;
        try {
            userPo = userMapper.selectOne(queryWrapper);
            return new ReturnObject<>(userPo != null);
        } catch (Exception e) {
            return new ReturnObject<>(userPo != null);
        }
    }

    public Long getid(String addr) {
        QueryWrapper<UserPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(UserPo.COL_ADDR, addr);
        return userMapper.selectOne(queryWrapper).getId();
    }

    public ReturnObject<Object> getPersonal(CyberInviter cyberInviter) {
        QueryWrapper<UserPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(UserPo.COL_INVSTRING, cyberInviter.getId());
        List userPos = userMapper.selectList(queryWrapper);
        List<Map<String, Object>> list = JSON.parseObject(JSON.toJSONString(userPos),
                new TypeReference<List<Map<String, Object>>>() {
                });
        for (int i = 0; i < list.size(); i++) {
            String addr = String.valueOf(list.get(i).get("addr"));
            String hashrate = String.valueOf(get(addr));

            if (redisUtils.hasKey("connectWallet-" + addr)) {
                list.get(i).put("connectWallet", true);
            } else {
                list.get(i).put("connectWallet", false);
            }

            list.get(i).put("hashrate", hashrate);
            long expire = redisUtils.getExpire("connectWallet-" + addr);
            long onlineTime = 24 * 60 * 60 - expire;
            list.get(i).put("onlineTime", onlineTime);
        }
        String inviterCode = cyberInviter.getInviterCode();
        Map inviterCodemap = new HashMap();
        inviterCodemap.put("inviterCode", inviterCode);
        list.add(inviterCodemap);
        return new ReturnObject<>(list);
    }


    public Object get(String address) {
        Map<String, Integer> map = new HashMap<>();
        map.put("connectWallet", 2);
        map.put("loginGame", 2);
        map.put("buyBox", 1);
        map.put("downloadGame", 0);

        int hashrate = 0;
        for (int i = 0; i < list.size(); i++) {
            boolean b = redisUtils.hasKey(list.get(i) + "-" + address);
            if (b) {
                hashrate += map.get(list.get(i));
            }
        }
        return "算力" + hashrate;
    }

    public List findAllAddress() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("addr");
        List list = userMapper.selectList(queryWrapper);
        return list;
    }


    public void saveCoin(int mubaisize, int fujisize, String address) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.set(UserPo.COL_MUBAICOIN, mubaisize);
        updateWrapper.set(UserPo.COL_FUJICOIN, fujisize);
        updateWrapper.eq(UserPo.COL_ADDR, address);

        userMapper.update(null, updateWrapper);
    }

    public void updateDownload(String address) {
        UpdateWrapper<UserPo> updateDownload = new UpdateWrapper<UserPo>();
        updateDownload.set(UserPo.COL_DOWNLOAD, 1);
        updateDownload.eq("addr", address);
        userMapper.update(null, updateDownload);
    }
}

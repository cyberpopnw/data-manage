package cyber.dealer.sys.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cyber.dealer.sys.constant.ReturnObject;
import cyber.dealer.sys.domain.CyberAgency;
import cyber.dealer.sys.domain.CyberUsers;
import cyber.dealer.sys.exception.ExceptionCast;
import cyber.dealer.sys.mapper.CyberAgencyMapper;
import cyber.dealer.sys.service.CyberUsersService;
import cyber.dealer.sys.mapper.CyberUsersMapper;
import cyber.dealer.sys.util.ObjectToMapUtil;
import cyber.dealer.sys.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.WalletUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static cyber.dealer.sys.constant.ReturnNo.*;
import static cyber.dealer.sys.util.Common.getRandomString;

/**
 * @author lfy
 * @description 针对表【cyber_users】的数据库操作Service实现
 * @createDate 2022-04-22 16:33:47
 */
@Service
public class CyberUsersServiceImpl extends ServiceImpl<CyberUsersMapper, CyberUsers>
        implements CyberUsersService {

    private final static List list = new ArrayList<String>() {
        {
            add("connectWallet");
            add("loginGame");
            add("buyBox");
        }
    };

    @Autowired
    private CyberUsersMapper cyberUsersMapper;

    @Autowired
    private CyberAgencyMapper cyberAgencyMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnObject<Object> inviter(String addr) {

        if (!WalletUtils.isValidAddress(addr)) {
            // 不合法直接返回错误
            ExceptionCast.cast(AUTH_INVALID_ADDR);
        }
        QueryWrapper<CyberUsers> queryWrapper = new QueryWrapper<>();
        CyberUsers cyberUsers1 = cyberUsersMapper.selectOne(queryWrapper);
        if (cyberUsers1 != null) {
            if (cyberUsers1.getAddress().compareToIgnoreCase(addr) == 0) {
                ExceptionCast.cast(AUTH_INVALID_ADDRS);
            }
        }


        CyberUsers cyberUsers = new CyberUsers();
        cyberUsers.setAddress(addr);
        cyberUsers.setLevel(1);
        cyberUsersMapper.insert(cyberUsers);
        CyberAgency cyberAgency = new CyberAgency();
        cyberAgency.setAddress(addr);
        cyberAgency.setUid(cyberUsers.getId());
        //1级用户有4个邀请码
        //1 邀请全球代理
        //2 邀请区域代理
        //3 邀请用户
        String inv1 = getRandomString(8);
        String inv2 = getRandomString(7);
        String inv3 = getRandomString(6);
        cyberAgency.setOneClass(inv1);
        cyberAgency.setTwoClass(inv2);
        cyberAgency.setThreeClass(inv3);
        cyberAgencyMapper.insert(cyberAgency);
        return new ReturnObject<Object>(cyberAgency);
    }

    @Override
    public ReturnObject<Object> invitation(String addr, String icode) {
        if (!WalletUtils.isValidAddress(addr)) {
            // 不合法直接返回错误
            ExceptionCast.cast(AUTH_INVALID_ADDR);
        }

        QueryWrapper<CyberAgency> queryWrapper = new QueryWrapper<>();
        int level = 0;
        if (icode != null) {
            if (icode.length() == 8) {
                queryWrapper.eq("one_class", icode);
                level = 2;
            } else if (icode.length() == 7) {
                queryWrapper.eq("two_class", icode);
                level = 3;
            } else if (icode.length() == 6) {
                queryWrapper.eq("three_class", icode);
                level = 4;
            } else {
                //用户无邀请码
                ExceptionCast.cast(AUTH_INVALID_CODE);
            }
        }
        //在这里写判断上一级是否有徽章



        CyberAgency cyberAgency = cyberAgencyMapper.selectOne(queryWrapper);
        if (cyberAgency == null) {
            ExceptionCast.cast(AUTH_INVALID_CODE);
        }
        //如果被邀请人为用户或者管理必须先删除在进行添加
        QueryWrapper<CyberUsers> queryWrappers = new QueryWrapper<>();
        queryWrappers.eq("address", addr);
        CyberUsers cyberUsers = cyberUsersMapper.selectOne(queryWrappers);
        QueryWrapper<CyberAgency> queryWrapperss = new QueryWrapper<>();
        queryWrapperss.eq("address", addr);
        CyberAgency cyberAgency1 = cyberAgencyMapper.selectOne(queryWrapperss);
        if (cyberUsers != null || cyberAgency1 != null) {
            ExceptionCast.cast(AUTH_INVALID_RECODE);
        }
        //条件不成立 创建
        return createInvitation(addr, level, cyberAgency.getId());
    }

    @Override
    public ReturnObject<Object> getData(String addr) {
        if (!WalletUtils.isValidAddress(addr)) {
            // 不合法直接返回错误
            ExceptionCast.cast(AUTH_INVALID_ADDR);
        }

        QueryWrapper<CyberUsers> CyberUsersQ = new QueryWrapper<>();
        CyberUsersQ.eq("address", addr);
        CyberUsers cyberUsers = cyberUsersMapper.selectOne(CyberUsersQ);
        if (cyberUsers == null) {
            ExceptionCast.cast(AUTH_INVALID_EQOBJ);
        }
        //获取等级
        Integer level = cyberUsers.getLevel();

        QueryWrapper<CyberAgency> CyberAgencyQ = new QueryWrapper<>();
        CyberAgencyQ.eq("uid", cyberUsers.getId());
        CyberAgency cyberAgency = cyberAgencyMapper.selectOne(CyberAgencyQ);
        if (cyberAgency == null) {
            ExceptionCast.cast(AUTH_INVALID_EQOBJ);
        }
        CyberUsersQ.clear();
        CyberUsersQ.eq("inv_id", cyberAgency.getId());
        List<CyberUsers> cyberUsers1 = cyberUsersMapper.selectList(CyberUsersQ);

        Map<String, Object> map = new HashMap();
        List list2 = new ArrayList();
        List list3 = new ArrayList();
        List list4 = new ArrayList();
        for (CyberUsers cyberUserss : cyberUsers1) {
            Map<String, String> convert = ObjectToMapUtil.convert(cyberUserss);
            convert.putAll(setRedisTo(cyberUserss.getAddress()));
            if (cyberUserss.getLevel() == 2) {
                list2.add(convert);
            } else if (cyberUserss.getLevel() == 3) {
                list3.add(convert);
            } else if (cyberUserss.getLevel() == 4) {
                list4.add(convert);
            }
        }
        if (list2.size() != 0) {
            map.put("level2", list2);
        }
        if (list3.size() != 0) {
            map.put("level3", list3);
        }
        if (list4.size() != 0) {
            map.put("level4", list4);
        }
        map.put("twoClass", cyberAgency.getTwoClass());
        map.put("threeClass", cyberAgency.getThreeClass());
        return new ReturnObject<>(map);
    }

    private Map setRedisTo(String address) {
        Map map = new HashMap();
        String hashrate = String.valueOf(get(address));
        //connectWallet-0x9b50b668dBa78DD61857e0137412DB6C2dF56016
        if (redisUtils.hasKey("connectWallet-" + address)) {
            map.put("connectWallet", true);
        } else {
            map.put("connectWallet", false);
        }

        map.put("hashrate", hashrate);
        long expire = redisUtils.getExpire("connectWallet-" + address);
        long onlineTime = 24 * 60 * 60 - expire;
        map.put("onlineTime", onlineTime);
        return map;
    }

    @Override
    public ReturnObject<Object> eqAddress(String addr) {

        if (!WalletUtils.isValidAddress(addr)) {
            // 不合法直接返回错误
            ExceptionCast.cast(AUTH_INVALID_ADDR);
        }

        QueryWrapper<CyberUsers> cyberUsers = new QueryWrapper<>();
        cyberUsers.eq("address", addr);
        CyberUsers cyberUsers1 = cyberUsersMapper.selectOne(cyberUsers);
        if (cyberUsers1 == null) {
            ExceptionCast.cast(AUTH_INVALID_ADDR);
        }
        if (cyberUsers1.getLevel() == 4) {
            ExceptionCast.cast(AUTH_INVALID_EQQX);
        }
        StpUtil.login(cyberUsers1.getId());
        return new ReturnObject<>(true);
    }

    @Override
    public ReturnObject<Object> outAddress(String address) {
        QueryWrapper<CyberUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("address", address);
        CyberUsers cyberUsers = cyberUsersMapper.selectOne(queryWrapper);
        if (cyberUsers == null) {
            ExceptionCast.cast(AUTH_INVALID_EQADMINEX);
        }
        StpUtil.logout(cyberUsers.getId());
        return new ReturnObject<>(true);
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

    @Transactional(rollbackFor = Exception.class)
    public ReturnObject<Object> createInvitation(String addr, int level, Long uid) {

        //当前等级不能邀请当前等级
        CyberAgency cyberAgencys = new CyberAgency();
        CyberUsers cyberUserss = new CyberUsers();
        String inv2 = getRandomString(7);
        String inv3 = getRandomString(6);
        if (level == 2) {
            //国家级
            cyberAgencys.setTwoClass(inv2);
            cyberAgencys.setThreeClass(inv3);
        } else if (level == 3) {
            //区域级
            cyberAgencys.setThreeClass(inv3);
        } else if (level == 4) {
            //用户
            cyberUserss.setLevel(level);
            cyberUserss.setInvId(uid);
            cyberUserss.setAddress(addr);
            cyberUsersMapper.insert(cyberUserss);
            return new ReturnObject<>(cyberUserss);
        }
        cyberUserss.setLevel(level);
        cyberUserss.setInvId(uid);
        cyberUserss.setAddress(addr);
        cyberUsersMapper.insert(cyberUserss);

        cyberAgencys.setAddress(addr);
        cyberAgencys.setUid(cyberUserss.getId());
        cyberAgencyMapper.insert(cyberAgencys);
        return new ReturnObject<>(cyberAgencys);
    }
}





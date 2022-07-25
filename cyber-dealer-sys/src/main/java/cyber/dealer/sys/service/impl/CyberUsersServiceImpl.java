package cyber.dealer.sys.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cyber.dealer.sys.constant.ReturnObject;
import cyber.dealer.sys.domain.CyberAgency;
import cyber.dealer.sys.domain.CyberUsers;
import cyber.dealer.sys.domain.CyberUsersRemarks;
import cyber.dealer.sys.exception.ExceptionCast;
import cyber.dealer.sys.mapper.CyberAgencyMapper;
import cyber.dealer.sys.mapper.CyberUsersRemarksMapper;
import cyber.dealer.sys.service.CyberUsersService;
import cyber.dealer.sys.mapper.CyberUsersMapper;
import cyber.dealer.sys.util.ObjectToMapUtil;
import cyber.dealer.sys.util.RedisUtils;
import io.reactivex.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletUtils;

import java.util.*;
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
            add("durationGame");
            add("downloadGame");
        }
    };

    @Autowired
    private CyberUsersRemarksMapper cyberUsersRemarksMapper;

    @Autowired
    private CyberUsersMapper cyberUsersMapper;

    @Autowired
    private CyberAgencyMapper cyberAgencyMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public ReturnObject<Object> invitation(String addr, String icode, String email, String nickname) {
//        if (!WalletUtils.isValidAddress(addr)) {
//            // 不合法直接返回错误
//            ExceptionCast.cast(AUTH_INVALID_ADDR);
//        }
        if (email.isEmpty()) {
            email = "cyberpop_email";
        }
        if (nickname.isEmpty()) {
            nickname = "cyberpop_user";
        }

        System.out.println(email);
        System.out.println(nickname);

        QueryWrapper<CyberAgency> queryWrapper = new QueryWrapper<>();
        int level = 0;
        CyberAgency cyberAgency = null;
        if (icode != null) {
            if (icode.length() == 8) {
                queryWrapper.eq("one_class", icode);
                level = 4;
                //用来邀请区域级
            } else if (icode.length() == 7) {
                queryWrapper.eq("two_class", icode);
                level = 3;
                //用来邀请伙伴级级
            } else if (icode.length() == 6) {
                queryWrapper.eq("three_class", icode);
                level = 2;
                //用来邀请用户级级
            } else {
                //用户无邀请码
                ExceptionCast.cast(AUTH_INVALID_CODE);
            }
            cyberAgency = cyberAgencyMapper.selectOne(queryWrapper);
            if (cyberAgency == null) {
                ExceptionCast.cast(AUTH_INVALID_CODE);
            }
        } else {
            ExceptionCast.cast(AUTH_INVALID_CODE);
        }

        //如果被邀请人为用户或者管理必须先删除在进行添加
        QueryWrapper<CyberUsers> queryWrappers = new QueryWrapper<>();
        queryWrappers.eq("address", addr);
        CyberUsers cyberUsers = cyberUsersMapper.selectOne(queryWrappers);
//        QueryWrapper<CyberAgency> queryWrapperss = new QueryWrapper<>();
//        queryWrapperss.eq("address", addr);
//        CyberAgency cyberAgency1 = cyberAgencyMapper.selectOne(queryWrapperss);
        if (cyberUsers != null) {
            ExceptionCast.cast(AUTH_INVALID_RECODE);
        }
        //条件不成立 创建
        return createInvitation(addr, level, cyberAgency.getId(), email, nickname);
    }

//    private void DetermineThereisBadge(Integer level1, String address) {
//        if (level1 == 1) {
//            level1 = 2;
//        }
//        String fujiUrl = "";
//        //0x76110C4d5c5a7Fe4Db304e35593490822701F484 地址
////        fujiUrl = "https://testquery.cyberpop.online/balanceOf?chainId=43113&contractAddress=0xD4c27B5A5c15B1524FC909F0FE0d191C4e893695&Id=" + level1 + "&account=" + address;
//        fujiUrl = "https://testquery.cyberpop.online/balanceOf?chainId=43113&contractAddress=0x586eba6be3ffc2499df154aef81b6d3a342c8e34&Id=" + level1 + "&account=" + address;
//        //在这里写判断上一级是否有徽章
//        //https://testquery.cyberpop.online/balanceOf?chainId=1&contractAddress=1&Id=1&account=1
//        String fujiQ = HttpURLConnectionUtil.doGet(fujiUrl);
//        Map fujimap = JSON.parseObject(fujiQ, Map.class);
//        Integer fujidata = Integer.valueOf(String.valueOf(fujimap.get("data")));
//        if (fujidata == 0) {
//            ExceptionCast.cast(AUTH_INVALID_NODELAY);
//        }
//    }

    @Override
    public ReturnObject<Object> getData(String email) {
//        if (!WalletUtils.isValidAddress(addr)) {
//            // 不合法直接返回错误
//            ExceptionCast.cast(AUTH_INVALID_ADDR);
//        }
//        addr = Keys.toChecksumAddress(addr);
        QueryWrapper<CyberUsers> CyberUsersQ = new QueryWrapper<>();
        CyberUsersQ.eq("email", email);
        CyberUsers cyberUsers = cyberUsersMapper.selectOne(CyberUsersQ);
        if (cyberUsers == null) {
            ExceptionCast.cast(AUTH_INVALID_EQOBJ);
        }

        QueryWrapper<CyberAgency> CyberAgencyQ = new QueryWrapper<>();
        CyberAgencyQ.eq("uid", cyberUsers.getId());
        CyberAgency cyberAgency = cyberAgencyMapper.selectOne(CyberAgencyQ);
        if (cyberAgency == null) {
            ExceptionCast.cast(AUTH_INVALID_EQOBJ);
        }
        CyberUsersQ.clear();
        CyberUsersQ.eq("inv_id", cyberAgency.getId());
        List<CyberUsers> cyberUsers1 = cyberUsersMapper.selectList(CyberUsersQ);

        //取出所有的备注
        List<CyberUsersRemarks> cyberUsersRemarks = cyberUsersRemarksMapper.selectList(null);
//        System.out.println(cyberUsersRemarks);
        Map<String, Object> map = new HashMap();
        List list2 = new ArrayList();
        List list3 = new ArrayList();
        List list4 = new ArrayList();
        List list1 = new ArrayList();
        for (CyberUsers cyberUserss : cyberUsers1) {
            Map<String, String> convert = ObjectToMapUtil.convert(cyberUserss);
            //备注赋予
            for (CyberUsersRemarks cyberUsersRemarks1 : cyberUsersRemarks) {
                if (cyberUsersRemarks1.getAddress().equals(email)) {
                    if (convert.get("email").equals(cyberUsersRemarks1.getToaddress())) {
                        convert.put("remarks", cyberUsersRemarks1.getRemarks());
                        break;
                    }
                }
            }
            System.out.println(cyberUserss.getId());
            QueryWrapper<CyberAgency> cyq = new QueryWrapper<>();
            cyq.eq("uid", cyberUserss.getId());
            CyberAgency ca = cyberAgencyMapper.selectOne(cyq);
            int level1 = 0;
            int level2 = 0;
            int level3 = 0;
            if (ca != null) {
                QueryWrapper<CyberUsers> cyuq = new QueryWrapper<>();
                cyuq.eq("inv_id", ca.getId());
                List<CyberUsers> cyberUsers2 = cyberUsersMapper.selectList(cyuq);
                for (CyberUsers cyberUsersss : cyberUsers2) {
                    if (cyberUsersss.getLevel() == 1) {
                        level1 += 1;
                    }
                    if (cyberUsersss.getLevel() == 2) {
                        level2 += 1;
                    }
                    if (cyberUsersss.getLevel() == 3) {
                        level3 += 1;
                    }
                }
                convert.put("level1", String.valueOf(level1));
                convert.put("level2", String.valueOf(level2));
                convert.put("level3", String.valueOf(level3));
            }


            convert.putIfAbsent("remarks", "cyber_user");
            convert.putAll(setRedisTo(cyberUserss.getAddress()));
            if (cyberUserss.getLevel() == 4) {
                list2.add(convert);
            } else if (cyberUserss.getLevel() == 3) {
                list3.add(convert);
            } else if (cyberUserss.getLevel() == 2) {
                list4.add(convert);
            } else if (cyberUserss.getLevel() == 1) {
                list1.add(convert);
            }
        }
        if (list2.size() != 0) {
            map.put("level4", list2);
        }
        if (list3.size() != 0) {
            map.put("level3", list3);
        }
        if (list4.size() != 0) {
            map.put("level2", list4);
        }
        if (list1.size() != 0) {
            map.put("level1", list1);
        }

        map.put("twoClass", cyberAgency.getTwoClass());
        map.put("threeClass", cyberAgency.getThreeClass());
        map.put("OneClass", cyberAgency.getOneClass());
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
        long expire1 = redisUtils.getExpire("loginGame-" + address);
        long gameTime = 24 * 60 * 60 - expire1;

        map.put("onlineTime", onlineTime);
        map.put("gameTime", gameTime);
        return map;
    }

    @Override
    public ReturnObject<Object> eqAddress(String addr) {

//        if (!WalletUtils.isValidAddress(addr)) {
//            // 不合法直接返回错误
//            ExceptionCast.cast(AUTH_INVALID_ADDR);
//        }
        addr = Keys.toChecksumAddress(addr);
        QueryWrapper<CyberUsers> cyberUsers = new QueryWrapper<>();
        cyberUsers.eq("address", addr);
        CyberUsers cyberUsers1 = cyberUsersMapper.selectOne(cyberUsers);

        if (cyberUsers1 == null) {
            ExceptionCast.cast(AUTH_INVALID_ADDR);
        }
//        if (StpUtil.isDisable(cyberUsers1.getId())) {
//            return new ReturnObject<>("该账号已被封禁");
//        }
        if (cyberUsers1.getLevel() == 1) {
            ExceptionCast.cast(AUTH_INVALID_EQQX);
        }

//        DetermineThereisBadge(cyberUsers1.getLevel(), addr);
        StpUtil.login(cyberUsers1.getId());
        Map map = new HashMap();
        List list1 = new ArrayList();
//        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
//        map.put(tokenInfo.getTokenName(), tokenInfo.getTokenValue());
        Map<String, String> convert = ObjectToMapUtil.convert(cyberUsers1);
        map.putAll(convert);
        list1.add(map);
        list1.add(true);
        return new ReturnObject<>(list1);
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

    @Override
    public ReturnObject<Object> findAll(String address) {
        LambdaQueryWrapper<CyberUsers> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CyberUsers::getAddress, address);
        CyberUsers cyberUsers = cyberUsersMapper.selectOne(queryWrapper);

        if (cyberUsers == null) {
            return new ReturnObject<>("无数据");
        }

        if (StpUtil.isDisable(cyberUsers.getId())) {
            ExceptionCast.cast(AUTH_INVALID_BANNED);
        }

        LambdaQueryWrapper<CyberAgency> cyberAgencyLambdaQueryWrapper = new LambdaQueryWrapper<>();
        cyberAgencyLambdaQueryWrapper.eq(CyberAgency::getId, cyberUsers.getId());
        CyberAgency cyberAgency = cyberAgencyMapper.selectOne(cyberAgencyLambdaQueryWrapper);

        queryWrapper.clear();
        queryWrapper.eq(CyberUsers::getInvId, cyberAgency.getId());
        List<CyberUsers> cyberUsers1 = cyberUsersMapper.selectList(queryWrapper);

        //把第一级的做成Map
        Map<String, String> convert = ObjectToMapUtil.convert(cyberUsers);
        Integer level = cyberUsers.getLevel();//level确定他有几级分类
        if (cyberUsers1.size() == 0) {
            return new ReturnObject<>(cyberUsers);
        }

        for (CyberUsers cyberUs : cyberUsers1) {
            if (cyberUs.getLevel() == 1) {

            }
        }
        return null;
    }

    @Override
    public Object setNikename(String nikename, String email) {
        LambdaUpdateWrapper<CyberUsers> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper
                .eq(CyberUsers::getEmail, email)
                .set(CyberUsers::getNikename, nikename)
        ;
        CyberUsers cyberUsers = new CyberUsers();
        cyberUsers.setNikename(nikename);
        return cyberUsersMapper.update(cyberUsers, queryWrapper) == 1;
    }

    @Override
    public Object getuser(CyberUsers one) {
        Map map = setRedisTo(one.getAddress());
        Map<String, String> convert = ObjectToMapUtil.convert(one);
        convert.putAll(map);
        return convert;
    }

    @Override
    public ReturnObject<Object> doLoginEmail(@NonNull String email, @NonNull String password) {
        LambdaQueryWrapper<CyberUsers> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CyberUsers::getEmail, email);
        queryWrapper.eq(CyberUsers::getPassword, password);
        CyberUsers cyberUsers = cyberUsersMapper.selectOne(queryWrapper);

        if (cyberUsers.getEmail() == null) {
            return new ReturnObject<>(AUTH_EMAIL_PASSWORD_FAIL);
        }

        if (cyberUsers.getLevel() == 1) {
            return new ReturnObject<>(AUTH_INVALID_EQLEVEL);
        }

        if (cyberUsers.getAddress() != null) {
            StpUtil.login(cyberUsers.getId());
        }
        Map map = new HashMap();
        List list1 = new ArrayList();
        Map<String, String> convert = ObjectToMapUtil.convert(cyberUsers);
        map.putAll(convert);
        list1.add(map);
        list1.add(true);
        return new ReturnObject<>(list1);
    }


    public Object get(String address) {
        Map<String, Integer> map = new HashMap<>();
        map.put("connectWallet", 2);
        map.put("loginGame", 1);
        map.put("buyBox", 1);
        map.put("durationGame", 1);
        map.put("downloadGame", 2);

        int hashrate = 0;
        for (int i = 0; i < list.size(); i++) {
            boolean b = redisUtils.hasKey(list.get(i) + "-" + address);
            if (b) {
                hashrate += map.get(list.get(i));
            }
        }
        return hashrate;
    }

    @Transactional(rollbackFor = Exception.class)
    public ReturnObject<Object> createInvitation(String addr, int level, Long uid, String email, String nickname) {

        //当前等级不能邀请当前等级
        CyberAgency cyberAgencys = new CyberAgency();
        CyberUsers cyberUserss = new CyberUsers();
        String inv2 = getRandomString(7);
        String inv3 = getRandomString(6);
        if (level == 4) {
            //区域
            cyberAgencys.setTwoClass(inv2);
            cyberAgencys.setThreeClass(inv3);
        } else if (level == 3) {
            //国家
            cyberAgencys.setThreeClass(inv3);
        } else if (level == 2) {
            //用户
            cyberUserss.setNikename(nickname);
            cyberUserss.setEmail(email);
            cyberUserss.setLevel(level - 1);
            cyberUserss.setInvId(uid);
            cyberUserss.setAddress(addr);
            cyberUserss.setCreateTime(new Date());
            cyberUserss.setUpdateTime(new Date());
            cyberUsersMapper.insert(cyberUserss);
            return new ReturnObject<>(cyberUserss);
        }

        cyberUserss.setEmail(email);
        cyberUserss.setNikename(nickname);
        cyberUserss.setLevel(level - 1);
        cyberUserss.setInvId(uid);
        cyberUserss.setAddress(addr);
        cyberUserss.setCreateTime(new Date());
        cyberUserss.setUpdateTime(new Date());
        cyberUsersMapper.insert(cyberUserss);

        cyberAgencys.setAddress(addr);
        cyberAgencys.setUid(cyberUserss.getId());
        cyberAgencys.setCreateTime(new Date());
        cyberAgencys.setUpdateTime(new Date());
        cyberAgencyMapper.insert(cyberAgencys);
        return new ReturnObject<>(cyberAgencys);
    }
}





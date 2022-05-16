package cyber.dealer.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import cyber.dealer.sys.domain.CyberDealersSystem;
import cyber.dealer.sys.domain.CyberUsers;
import cyber.dealer.sys.domain.CyberUsersRecord;
import cyber.dealer.sys.domain.vo.GeneralFormatVo;
import cyber.dealer.sys.mapper.CyberDealersSystemMapper;
import cyber.dealer.sys.mapper.CyberUsersMapper;
import cyber.dealer.sys.mapper.CyberUsersRecordMapper;
import cyber.dealer.sys.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.Keys;

import java.util.*;

/**
 * @author lfy
 * @Date 2022/4/25 11:59
 */
@RestController
@RequestMapping("connection")
@CrossOrigin
public class CalculateTheForceController {

    private final static List list = new ArrayList<String>() {
        {
            add("connectWallet");
            add("loginGame");
            add("buyBox");
            add("durationGame");
            add("downloadGame");
        }
    };
    private final static Map<String, Integer> map = new HashMap() {
        {
            put("connectWallet", 2);
            put("loginGame", 1);
            put("buyBox", 1);
            put("durationGame", 1);
            put("downloadGame", 2);
        }
    };

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private CyberUsersRecordMapper cyberUsersRecordMapper;

    @Autowired
    private CyberUsersMapper cyberUsersMapper;

    @Autowired
    private CyberDealersSystemMapper cyberDealersSystemMapper;

    @PostMapping("general")
    public Object setGeneral(@RequestBody GeneralFormatVo generalFormatVo) {
        generalFormatVo.setAddress(Keys.toChecksumAddress(generalFormatVo.getAddress()));
        if (!list.contains(generalFormatVo.getAction())) {
            return false;
        }
        //过期时间代表一天
        redisUtils.set(generalFormatVo.getAction() + "-" + generalFormatVo.getAddress()
                , JSONObject.toJSONString(generalFormatVo), 24 * 60 * 60);


        CyberUsersRecord cyberUsersRecord = new CyberUsersRecord();
        cyberUsersRecord.setAction(generalFormatVo.getAction());
        cyberUsersRecord.setAddress(generalFormatVo.getAddress());
        cyberUsersRecord.setTime(new Date());
        cyberUsersRecord.setParameter1(generalFormatVo.getParameter1());
        cyberUsersRecord.setParameter2(generalFormatVo.getParameter2());
        cyberUsersRecord.setParameter3(generalFormatVo.getParameter3());
        cyberUsersRecordMapper.insert(cyberUsersRecord);

        if ("durationGame".equals(generalFormatVo.getAction())) {
            QueryWrapper<CyberUsers> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("address", generalFormatVo.getAddress());
            CyberUsers cyberUsers1 = cyberUsersMapper.selectOne(queryWrapper);

            UpdateWrapper<CyberUsers> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("address", generalFormatVo.getAddress())
                    .set("playgametimes", cyberUsers1.getPlaygametimes() + Long.parseLong(generalFormatVo.getParameter1()));
            CyberUsers cyberUsers = new CyberUsers();
            cyberUsers.setAddress(generalFormatVo.getAddress());
            Long bLong = Long.valueOf(generalFormatVo.getParameter1());
            cyberUsers.setPlaygametimes(cyberUsers1.getPlaygametimes() + bLong);
            cyberUsersMapper.update(cyberUsers, updateWrapper);
        }
        return true;
    }

    @GetMapping("calculateTotalForce")
    public Object getCalculateTotalForce() {
        Map<Object, Object> map1 = new HashMap();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("systemKey", "CalculateTheReward");
        CyberDealersSystem cyberDealersSystem = cyberDealersSystemMapper.selectOne(queryWrapper);
        String systemval = cyberDealersSystem.getSystemval();
        String calculate = redisUtils.get("总算力");
        //奖励
        map1.put("Calculate the reward", systemval);
        //全部总算力
        map1.put("calculate", calculate);
        //当前全网 /s  奖励
        map1.put("personal calculate", Double.valueOf(systemval) / 24 / 60 / 60 / Double.valueOf(calculate));
        return map1;
    }


}

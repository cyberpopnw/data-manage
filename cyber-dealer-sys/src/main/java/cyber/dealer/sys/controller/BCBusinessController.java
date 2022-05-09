package cyber.dealer.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cyber.dealer.sys.constant.ReturnObject;
import cyber.dealer.sys.domain.CyberAgency;
import cyber.dealer.sys.domain.CyberUsers;
import cyber.dealer.sys.exception.ExceptionCast;
import cyber.dealer.sys.mapper.CyberAgencyMapper;
import cyber.dealer.sys.mapper.CyberUsersMapper;
import jnr.ffi.annotations.Out;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static cyber.dealer.sys.constant.ReturnNo.*;
import static cyber.dealer.sys.util.Common.decorateReturnObject;
import static cyber.dealer.sys.util.Common.getRandomString;

/**
 * @author lfy
 * @Date 2022/5/4 0:28
 */
@RestController
@RequestMapping("business")
@CrossOrigin
public class BCBusinessController {

    @Autowired
    private CyberUsersMapper cyberUsersMapper;

    @Autowired
    private CyberAgencyMapper cyberAgencyMapper;


    //注册国家级的  level ==4
    @RequestMapping("nationallevel")
    public Object setNationallevel(String address,
                                   String nickname,
                                   String email,
                                   Integer level) {
        if (level != 4) {
            ExceptionCast.cast(AUTH_INVALID_EQLEVEL);
        }

        System.out.println(address);

        QueryWrapper<CyberUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("address", address.toLowerCase());
        CyberUsers cyberUsers1 = cyberUsersMapper.selectOne(queryWrapper);

        if (cyberUsers1 != null) {
            ExceptionCast.cast(AUTH_INVALID_ADDRS);
        }

        CyberUsers cyberUsers = new CyberUsers();
        cyberUsers.setAddress(address.toLowerCase());
        cyberUsers.setNikename(nickname);
        cyberUsers.setEmail(email);
        cyberUsers.setDobadge(1L);
        cyberUsers.setLevel(4);
        cyberUsers.setCreateTime(new Date());
        cyberUsers.setUpdateTime(new Date());

        cyberUsersMapper.insert(cyberUsers);

        CyberAgency cyberAgency = new CyberAgency();
        cyberAgency.setUid(cyberUsers.getId());
        String inv1 = getRandomString(8);
        String inv2 = getRandomString(7);
        String inv3 = getRandomString(6);
        cyberAgency.setOneClass(inv1);
        cyberAgency.setTwoClass(inv2);
        cyberAgency.setThreeClass(inv3);
        cyberAgency.setCreateTime(new Date());
        cyberAgency.setUpdateTime(new Date());
        cyberAgency.setAddress(address.toLowerCase());
        cyberAgencyMapper.insert(cyberAgency);
        return decorateReturnObject(new ReturnObject<>(cyberAgency));
//        return decorateReturnObject(cyberUsersService.inviter(addr.toLowerCase()));
    }

    //注册区域级级的  3
    @RequestMapping("arealevel")
    public Object setArea(String address,
                             String nickname,
                             String email,
                             Integer level) {
        if (level != 3) {
            ExceptionCast.cast(AUTH_INVALID_EQLEVEL);
        }

        QueryWrapper<CyberUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("address", address.toLowerCase());
        CyberUsers cyberUsers1 = cyberUsersMapper.selectOne(queryWrapper);

        if (cyberUsers1 != null) {
            ExceptionCast.cast(AUTH_INVALID_ADDRS);
        }

        CyberUsers cyberUsers = new CyberUsers();
        cyberUsers.setAddress(address.toLowerCase());
        cyberUsers.setNikename(nickname);
        cyberUsers.setEmail(email);
        cyberUsers.setDobadge(1L);
        cyberUsers.setLevel(3);
        cyberUsers.setCreateTime(new Date());
        cyberUsers.setUpdateTime(new Date());

        cyberUsersMapper.insert(cyberUsers);

        CyberAgency cyberAgency = new CyberAgency();
        cyberAgency.setUid(cyberUsers.getId());
        //伙伴级 只有3级密码
        String inv2 = getRandomString(7);
        String inv3 = getRandomString(6);
        cyberAgency.setTwoClass(inv2);
        cyberAgency.setThreeClass(inv3);
        cyberAgency.setCreateTime(new Date());
        cyberAgency.setUpdateTime(new Date());
        cyberAgency.setAddress(address.toLowerCase());
        cyberAgencyMapper.insert(cyberAgency);
        return decorateReturnObject(new ReturnObject<>(cyberAgency));
//        return decorateReturnObject(cyberUsersService.inviter(addr.toLowerCase()));
    }

    //注册伙伴级的  level == 2
    @RequestMapping("partnerlevel")
    public Object setPartner(String address,
                                   String nickname,
                                   String email,
                                   Integer level) {
        if (level != 2) {
            ExceptionCast.cast(AUTH_INVALID_EQLEVEL);
        }

        QueryWrapper<CyberUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("address", address.toLowerCase());
        CyberUsers cyberUsers1 = cyberUsersMapper.selectOne(queryWrapper);

        if (cyberUsers1 != null) {
            ExceptionCast.cast(AUTH_INVALID_ADDRS);
        }

        CyberUsers cyberUsers = new CyberUsers();
        cyberUsers.setAddress(address.toLowerCase());
        cyberUsers.setNikename(nickname);
        cyberUsers.setEmail(email);
        cyberUsers.setDobadge(1L);
        cyberUsers.setLevel(2);
        cyberUsers.setCreateTime(new Date());
        cyberUsers.setUpdateTime(new Date());

        cyberUsersMapper.insert(cyberUsers);

        CyberAgency cyberAgency = new CyberAgency();
        cyberAgency.setUid(cyberUsers.getId());
        //伙伴级 只有3级密码
        String inv3 = getRandomString(6);
        cyberAgency.setThreeClass(inv3);
        cyberAgency.setCreateTime(new Date());
        cyberAgency.setUpdateTime(new Date());
        cyberAgency.setAddress(address.toLowerCase());
        cyberAgencyMapper.insert(cyberAgency);
        return decorateReturnObject(new ReturnObject<>(cyberAgency));
//        return decorateReturnObject(cyberUsersService.inviter(addr.toLowerCase()));
    }




}

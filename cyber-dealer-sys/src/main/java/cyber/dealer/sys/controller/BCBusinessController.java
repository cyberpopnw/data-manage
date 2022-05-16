package cyber.dealer.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import cyber.dealer.sys.constant.ReturnObject;
import cyber.dealer.sys.domain.CyberAgency;
import cyber.dealer.sys.domain.CyberUsers;
import cyber.dealer.sys.exception.ExceptionCast;
import cyber.dealer.sys.mapper.CyberAgencyMapper;
import cyber.dealer.sys.mapper.CyberUsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.Keys;

import java.util.Date;
import java.util.Objects;

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
    @PostMapping("nationallevel")
    public Object setNationallevel(String address,
                                   String nickname,
                                   String email,
                                   Integer level) {
        address = Keys.toChecksumAddress(address);
        if (level != 4) {
            ExceptionCast.cast(AUTH_INVALID_EQLEVEL);
        }

        System.out.println(address);

        QueryWrapper<CyberUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("address", address);
        CyberUsers cyberUsers1 = cyberUsersMapper.selectOne(queryWrapper);

        if (cyberUsers1 != null) {
            ExceptionCast.cast(AUTH_INVALID_ADDRS);
        }

        CyberUsers cyberUsers = new CyberUsers();
        cyberUsers.setAddress(address);
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
        cyberAgency.setAddress(address);
        cyberAgencyMapper.insert(cyberAgency);
        return decorateReturnObject(new ReturnObject<>(cyberAgency));
//        return decorateReturnObject(cyberUsersService.inviter(addr.toLowerCase()));
    }

    //注册区域级级的  3
    @PostMapping("arealevel")
    public Object setArea(String address,
                          String nickname,
                          String email,
                          Integer level) {
        address = Keys.toChecksumAddress(address);
        if (level != 3) {
            ExceptionCast.cast(AUTH_INVALID_EQLEVEL);
        }

        QueryWrapper<CyberUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("address", address);
        CyberUsers cyberUsers1 = cyberUsersMapper.selectOne(queryWrapper);

        if (cyberUsers1 != null) {
            ExceptionCast.cast(AUTH_INVALID_ADDRS);
        }

        CyberUsers cyberUsers = new CyberUsers();
        cyberUsers.setAddress(address);
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
        cyberAgency.setAddress(address);
        cyberAgencyMapper.insert(cyberAgency);
        return decorateReturnObject(new ReturnObject<>(cyberAgency));
//        return decorateReturnObject(cyberUsersService.inviter(addr.toLowerCase()));
    }

    //注册伙伴级的  level == 2
    @PostMapping("partnerlevel")
    public Object setPartner(String address,
                             String nickname,
                             String email,
                             Integer level) {
        address = Keys.toChecksumAddress(address);
        if (level != 2) {
            ExceptionCast.cast(AUTH_INVALID_EQLEVEL);
        }

        QueryWrapper<CyberUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("address", address);
        CyberUsers cyberUsers1 = cyberUsersMapper.selectOne(queryWrapper);

        if (cyberUsers1 != null) {
            ExceptionCast.cast(AUTH_INVALID_ADDRS);
        }

        CyberUsers cyberUsers = new CyberUsers();
        cyberUsers.setAddress(address);
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
        cyberAgency.setAddress(address);
        cyberAgencyMapper.insert(cyberAgency);
        return decorateReturnObject(new ReturnObject<>(cyberAgency));
//        return decorateReturnObject(cyberUsersService.inviter(addr.toLowerCase()));
    }


    //注册伙伴级的  level == 1
    @GetMapping("userlevel")
    @Transactional(rollbackFor = Exception.class)
    public Object setUser(String address,
                          String email,
                          Integer level) {

        address = Keys.toChecksumAddress(address);
        System.out.println(address);
        if (level != 1) {
            ExceptionCast.cast(AUTH_INVALID_EQLEVEL);
        }

        LambdaQueryWrapper<CyberUsers> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CyberUsers::getAddress, address);
        queryWrapper.eq(CyberUsers::getEmail, email);
        CyberUsers cyberUsers1 = cyberUsersMapper.selectOne(queryWrapper);
//
//        LambdaQueryWrapper<CyberUsers> queryWrapper3 = new LambdaQueryWrapper<>();
//        queryWrapper3.eq(CyberUsers::getEmail, email);
//        CyberUsers cyberUsers3 = cyberUsersMapper.selectOne(queryWrapper);
//        queryWrapper.clear();
//        queryWrapper.eq(CyberUsers::getAddress, address);
//        CyberUsers cyberUsers4 = cyberUsersMapper.selectOne(queryWrapper);

        CyberUsers cyberUsers = new CyberUsers();
        System.out.println(cyberUsers);
        if (cyberUsers1 != null) {
            System.out.println("我有数据");
            LambdaUpdateWrapper<CyberUsers> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(CyberUsers::getAddress, address);
            updateWrapper.eq(CyberUsers::getEmail, email);

            cyberUsers = new CyberUsers();
            cyberUsers.setEmail(email);
            cyberUsers.setAddress(address);
            cyberUsers.setUpdateTime(new Date());
            cyberUsersMapper.update(cyberUsers, updateWrapper);
        } else {
            System.out.println("我没有数据");
            LambdaQueryWrapper<CyberUsers> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(CyberUsers::getEmail, email);
            CyberUsers cyberUsers2 = cyberUsersMapper.selectOne(queryWrapper1);
            queryWrapper1.clear();
            queryWrapper1.eq(CyberUsers::getAddress, address);
            CyberUsers cyberUsers3 = cyberUsersMapper.selectOne(queryWrapper1);

            if (cyberUsers2 != null && cyberUsers3 == null) {
                System.out.println("有邮箱");
                //有邮箱 需要看address不一样覆盖
                LambdaUpdateWrapper<CyberUsers> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.set(CyberUsers::getAddress, address);
                updateWrapper.eq(CyberUsers::getEmail, email);

                cyberUsers = new CyberUsers();
                cyberUsers.setAddress(address);
                cyberUsers.setEmail(email);
                cyberUsers.setUpdateTime(new Date());
                cyberUsersMapper.update(cyberUsers, updateWrapper);
            } else if (cyberUsers3 != null) {
                System.out.println("我有address");
                //有address  邮箱不一样
                ExceptionCast.cast(AUTH_INVALID_ADDRS);
            } else {
                cyberUsers.setAddress(address);
                cyberUsers.setEmail(email);
                cyberUsers.setDobadge(0L);
                cyberUsers.setLevel(level);
                cyberUsers.setCreateTime(new Date());
                cyberUsers.setUpdateTime(new Date());

                cyberUsersMapper.insert(cyberUsers);
            }
        }
        return decorateReturnObject(new ReturnObject<>(cyberUsers));
    }

    @PostMapping("invuser")
    public Object invUser(String address,
                          String email,
                          String icode,
                          String nickname) {

        System.out.println(address + email + icode + nickname);

        if (Objects.equals(address, "0")) {
            address = "";
        }

        if (Objects.equals(icode, "0")) {
            icode = "";
        }

        if (Objects.equals(nickname, "0")) {
            nickname = "";
        }

        address = Keys.toChecksumAddress(address);

        if (!nickname.isEmpty()) {
            nickname = "cyberpop_user";
        }


        LambdaQueryWrapper<CyberUsers> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CyberUsers::getEmail, email);
        CyberUsers cyberUsers1 = cyberUsersMapper.selectOne(queryWrapper);
        queryWrapper.clear();
        if (cyberUsers1 != null) {
            ExceptionCast.cast(AUTH_INVALID_EQEMAIL);
        }
        CyberUsers cyberUsers = new CyberUsers();
        CyberAgency cyberAgency = new CyberAgency();

        int level = 1;
        if ("0x".equals(address)) {
            address = "";
        } else {
            queryWrapper.eq(CyberUsers::getAddress, address);
            CyberUsers cyberUsers2 = cyberUsersMapper.selectOne(queryWrapper);

            if ( cyberUsers2 != null) {
                ExceptionCast.cast(AUTH_INVALID_EQADMINEX);
            }
        }

        if (!icode.isEmpty()) {
            LambdaQueryWrapper<CyberAgency> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            if (icode.length() == 6) {
                lambdaQueryWrapper.eq(CyberAgency::getThreeClass, icode);
            } else if (icode.length() == 7) {
                lambdaQueryWrapper.eq(CyberAgency::getTwoClass, icode);
                level = 2;
            } else if (icode.length() == 8) {
                lambdaQueryWrapper.eq(CyberAgency::getOneClass, icode);
                level = 3;
            } else {
                ExceptionCast.cast(AUTH_INVALID_CODE);
            }
            CyberAgency cyberAgencys = cyberAgencyMapper.selectOne(lambdaQueryWrapper);

            if (cyberAgencys == null) {
                ExceptionCast.cast(AUTH_INVALID_CODE);
            }

            cyberUsers.setInvId(cyberAgencys.getId());
            cyberUsers.setAddress(address);
            cyberUsers.setEmail(email);
            cyberUsers.setDobadge(0L);
            cyberUsers.setLevel(level);

        } else {
            cyberUsers.setAddress(address);
            cyberUsers.setEmail(email);
            cyberUsers.setDobadge(0L);
            cyberUsers.setLevel(1);
        }
        cyberUsers.setNikename(nickname);
        cyberUsers.setCreateTime(new Date());
        cyberUsers.setUpdateTime(new Date());
        cyberUsersMapper.insert(cyberUsers);
        if (level != 1) {
            cyberAgency.setUid(cyberUsers.getId());
            String inv3 = getRandomString(6);
            String inv2 = getRandomString(7);
            if (level == 2) {
                cyberAgency.setThreeClass(inv3);
            } else {
                cyberAgency.setThreeClass(inv2);
            }
            cyberAgency.setCreateTime(new Date());
            cyberAgency.setUpdateTime(new Date());
            cyberAgency.setAddress(address);
            cyberAgencyMapper.insert(cyberAgency);
        }

        return decorateReturnObject(new ReturnObject<>(cyberUsers));
//        return decorateReturnObject(cyberUsersService.inviter(addr.toLowerCase()));
    }


}

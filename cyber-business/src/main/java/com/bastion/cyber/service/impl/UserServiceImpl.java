package com.bastion.cyber.service.impl;

import com.bastion.cyber.config.IRefreshScopeConfig;
import com.bastion.cyber.constant.ReturnObject;
import com.bastion.cyber.dao.UserDao;
import com.bastion.cyber.exception.ExceptionCast;
import com.bastion.cyber.model.bo.InviterBo;
import com.bastion.cyber.model.bo.SignBo;
import com.bastion.cyber.model.dto.LoginUserDto;
import com.bastion.cyber.model.dto.ReportDto;
import com.bastion.cyber.model.dto.UserDto;
import com.bastion.cyber.model.po.CyberInviter;
import com.bastion.cyber.model.po.UserPo;
import com.bastion.cyber.model.vo.AccessTokenVo;
import com.bastion.cyber.model.vo.PreLoginUserVo;
import com.bastion.cyber.model.vo.UserVo;
import com.bastion.cyber.service.CyberInviterService;
import com.bastion.cyber.service.UserService;
import com.bastion.cyber.utils.Common;
import com.bastion.cyber.utils.Web3Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.WalletUtils;

import java.util.Objects;
import java.util.Optional;

import static com.bastion.cyber.constant.ReturnNo.AUTH_INVALID_ADDR;
import static com.bastion.cyber.constant.ReturnNo.AUTH_INVALID_JWT;
import static com.bastion.cyber.utils.Common.isOk;
import static com.bastion.cyber.utils.JwtHelper.createToken;

/**
 * Created on 2022/4/7.
 *
 * @author zyg
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final IRefreshScopeConfig iRefreshScopeConfig;
    private final UserDao userDao;
    private final CyberInviterService cyberInviterService;

    public UserServiceImpl(IRefreshScopeConfig iRefreshScopeConfig, UserDao userDao, CyberInviterService cyberInviterService) {
        this.iRefreshScopeConfig = iRefreshScopeConfig;
        this.userDao = userDao;
        this.cyberInviterService = cyberInviterService;
    }

    @Override
    public ReturnObject<Object> info(String addr) {
        UserDto userDto = new UserDto();
        userDto.setAddr(addr);
        ReturnObject<Object> byAddr = userDao.findBy(userDto);
        if (!isOk(byAddr)) {
            return byAddr;
        }
        UserPo userPo = findOrCreateUser(addr);
        String nonce = Common.genSeqNum(36);

        userDto.setNonce(nonce);
        userDto.setId(userPo.getId());

        userDao.updateBy(userDto);
        return new ReturnObject<>(PreLoginUserVo.builder().id(userPo.getId()).publicAddress(userPo.getAddr()).nonce(nonce).username(userPo.getName()).build());
    }

    @Override
    public ReturnObject<Object> auth(LoginUserDto loginUserDto) {
        String publicAddress = loginUserDto.getPublicAddress();
        String signature = loginUserDto.getSignature();
        String nonce = loginUserDto.getNonce();
        String inviterCode = loginUserDto.getInviterCode();
        loginAuth(publicAddress, signature, nonce);

        UserPo userPo = findOrCreateUser(publicAddress, inviterCode);

        userDao.updateBy(UserDto.builder().id(userPo.getId()).inviterId(userPo.getInviterId()).build());

        // 校验通过，返回登录信息
        String jwt = createToken(new SignBo(userPo.getId(), publicAddress), iRefreshScopeConfig.getJwtExpire().intValue());
        return new ReturnObject<>(new AccessTokenVo(jwt));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnObject<Object> inviter(String addr) {
        // 地址合法性校验
        if (!WalletUtils.isValidAddress(addr)) {
            // 不合法直接返回错误
            ExceptionCast.cast(AUTH_INVALID_ADDR);
        }
        ReturnObject<Object> createUserObj = userDao.create(UserPo.builder().addr(addr).role((byte) 2).build());
        if (!isOk(createUserObj)) {
            return createUserObj;
        }
        UserPo createUser = (UserPo) createUserObj.getData();
        ReturnObject<Object> createInviterObj = cyberInviterService.create(createUser.getId(), (byte) 2);
        if (!isOk(createInviterObj)) {
            return createInviterObj;
        }

        String jwt = createToken(new SignBo(createUser.getId(), addr), iRefreshScopeConfig.getJwtExpire().intValue());
        return new ReturnObject<>(new AccessTokenVo(jwt));
    }

    @Override
    public ReturnObject<Object> detail(Long userId) {
        UserDto userDto = new UserDto();
        userDto.setId(userId);
        ReturnObject<Object> byAddr = userDao.findBy(userDto);
        if (!isOk(byAddr)) {
            return byAddr;
        }
        UserVo data = Optional.ofNullable((UserPo) byAddr.getData())
                .map(user -> UserVo.builder().id(user.getId()).username(user.getName()).build())
                .orElse(UserVo.builder().build());
        return new ReturnObject<>(data);
    }

    @Override
    public ReturnObject<Object> update(UserDto userDto) {
        ReturnObject<Object> updateObj = userDao.updateBy(userDto);
        if (!isOk(updateObj)) {
            return updateObj;
        }
        ReturnObject<Object> userDaoBy = userDao.findBy(userDto);
        if (!isOk(userDaoBy)) {
            return userDaoBy;
        }
        UserVo data = Optional.ofNullable((UserPo) userDaoBy.getData())
                .map(user -> UserVo.builder().id(user.getId()).username(user.getName()).build())
                .orElse(UserVo.builder().build());
        return new ReturnObject<>(data);
    }

    @Override
    public ReturnObject<Object> report(ReportDto reportDto) {
        String inviterCode = reportDto.getInviterCode();
        String addr = reportDto.getAddr();
        UserPo userPo = findOrCreateUser(addr, inviterCode);

        userDao.updateBy(UserDto.builder().id(userPo.getId()).inviterId(userPo.getInviterId()).build());

        String jwt = createToken(new SignBo(userPo.getId(), addr), 3);
        return new ReturnObject<>(new AccessTokenVo(jwt));
    }

    private UserPo findOrCreateUser(String addr) {
        UserDto userDto = UserDto.builder().addr(addr).build();
        ReturnObject<Object> byAddr = userDao.findBy(userDto);
        if (!isOk(byAddr)) {
            ExceptionCast.cast(byAddr.getCode());
        }
        UserPo userPo = (UserPo) byAddr.getData();

        if (Objects.isNull(userPo)) {
            ReturnObject<Object> create = userDao.create(UserPo.builder().addr(addr).build());
            if (!isOk(create)) {
                ExceptionCast.cast(create.getCode());
            }
            userPo = (UserPo) create.getData();
        }
        return userPo;
    }

    private UserPo findOrCreateUser(String addr, String inviterCode) {
        ReturnObject<Object> byInviterCode = cyberInviterService.findBy(InviterBo.builder().inviterCode(inviterCode).build());
        CyberInviter cyberInviter = Optional.of(byInviterCode).map(row -> (CyberInviter) row.getData()).get();
        Long iUid = cyberInviter.getUid();
        Long inviterId = cyberInviter.getId();

        UserDto userDto = UserDto.builder().addr(addr).build();
        ReturnObject<Object> byAddr = userDao.findBy(userDto);
        if (!isOk(byAddr)) {
            ExceptionCast.cast(byAddr.getCode());
        }
        UserPo userPo = (UserPo) byAddr.getData();

        if (Objects.isNull(userPo)) {
            ReturnObject<Object> create = userDao.create(UserPo.builder().addr(addr).inviterId(inviterId).build());
            if (!isOk(create)) {
                ExceptionCast.cast(create.getCode());
            }
            userPo = (UserPo) create.getData();
        }
        Long uid = userPo.getId();
        Long uInviterId = userPo.getInviterId();
        if (Objects.isNull(uInviterId) && iUid.compareTo(uid) != 0) {
            userPo.setInviterId(inviterId);
        }
        if (uid.compareTo(iUid) == 0) {
            log.warn("不能邀请自己 uid:{}", uid);
        }
        return userPo;
    }

    private void loginAuth(String publicAddress, String signature, String nonce) {
        // 地址合法性校验
        if (!WalletUtils.isValidAddress(publicAddress)) {
            // 不合法直接返回错误
            ExceptionCast.cast(AUTH_INVALID_ADDR);
        }
        // 校验签名信息
        if (!Web3Util.validate(signature, nonce, publicAddress)) {
            ExceptionCast.cast(AUTH_INVALID_JWT);
        }
    }
}

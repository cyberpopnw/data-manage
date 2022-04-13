package com.bastion.cyber.service;

import com.bastion.cyber.constant.ReturnObject;
import com.bastion.cyber.model.dto.LoginUserDto;
import com.bastion.cyber.model.dto.ReportDto;
import com.bastion.cyber.model.dto.UserDto;

/**
 * Created on 2022/4/7.
 *
 * @author zyg
 */
public interface UserService {
    ReturnObject<Object> info(String addr);

    ReturnObject<Object> auth(LoginUserDto loginUserDto);

    ReturnObject<Object> inviter(String addr);

    ReturnObject<Object> detail(Long userId);

    ReturnObject<Object> update(UserDto userDto);

    ReturnObject<Object> report(ReportDto reportDto);
}

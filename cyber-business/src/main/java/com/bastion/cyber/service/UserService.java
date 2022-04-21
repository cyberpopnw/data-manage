package com.bastion.cyber.service;

import com.bastion.cyber.constant.ReturnObject;
import com.bastion.cyber.model.dto.LoginUserDto;
import com.bastion.cyber.model.dto.ReportDto;
import com.bastion.cyber.model.dto.UserDto;

import java.util.List;

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

    ReturnObject<Object> getboolean(String addr);

    ReturnObject<Object> getPersonal(String addr);

    List findAllAddress();

    void saveCoin(int mubaisize, int fujisize, String o);

    void updateDownload(String address);
}

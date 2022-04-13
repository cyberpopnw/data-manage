package com.bastion.cyber.controller;

import com.bastion.cyber.annotation.aop.BastAuth;
import com.bastion.cyber.annotation.aop.BastAuthBo;
import com.bastion.cyber.annotation.aop.BastLogger;
import com.bastion.cyber.api.user.UserControllerApi;
import com.bastion.cyber.model.bo.AuthBo;
import com.bastion.cyber.model.dto.LoginUserDto;
import com.bastion.cyber.model.dto.ReportDto;
import com.bastion.cyber.model.dto.UserDto;
import com.bastion.cyber.service.UserService;
import org.springframework.web.bind.annotation.*;

import static com.bastion.cyber.utils.Common.decorateReturnObject;

/**
 * Created on 2022/4/7.
 *
 * @author zyg
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/user", produces = "application/json;charset=UTF-8")
public class UserController implements UserControllerApi {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    @GetMapping("/info")
    public Object info(@RequestParam("publicAddress") String addr) {
        return decorateReturnObject(userService.info(addr));
    }

    @Override
    @BastLogger
    @PostMapping("/auth")
    public Object auth(@RequestBody LoginUserDto loginUserDto) {
        return decorateReturnObject(userService.auth(loginUserDto));
    }

    @Override
    @GetMapping("/inviter")
    public Object inviter(@RequestParam String addr) {
        return decorateReturnObject(userService.inviter(addr));
    }

    @Override
    @BastAuth
    @GetMapping("/detail")
    public Object detail(@BastAuthBo("id") AuthBo authBo) {
        Long userId = authBo.getId();
        return decorateReturnObject(userService.detail(userId));
    }

    @Override
    @BastAuth
    @RequestMapping(value = "/update", method = RequestMethod.PATCH)
    public Object update(@BastAuthBo("id") AuthBo authBo, @RequestParam String name) {
        UserDto userDto = UserDto.builder().id(authBo.getId()).name(name).build();
        return decorateReturnObject(userService.update(userDto));
    }

    @Override
    @BastLogger
    @PostMapping("/report")
    public Object report(@RequestBody ReportDto reportDto) {
        return decorateReturnObject(userService.report(reportDto));
    }
}

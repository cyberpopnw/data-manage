package com.bastion.cyber.controller;

import com.bastion.cyber.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.bastion.cyber.utils.Common.decorateReturnObject;

/**
 * @author lfy
 * @Date 2022/4/13 18:02
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/getdata", produces = "application/json;charset=UTF-8")
public class ReDataListController {



    @Autowired
    private UserService userService;

    @GetMapping("login")
    public Object getLogin(@RequestParam("address") String addr) {
        return decorateReturnObject(userService.getboolean(addr));
    }

    @GetMapping("getpersonal")
    public Object getPersonal(@RequestParam("address") String addr) {
        return decorateReturnObject(userService.getPersonal(addr));
    }
}

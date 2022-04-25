package com.bastion.cyber.controller;

import com.alibaba.fastjson.JSONObject;
import com.bastion.cyber.model.vo.GeneralFormatVo;
import com.bastion.cyber.service.UserService;
import com.bastion.cyber.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lfy
 * @Date 2022/4/19 14:15
 */
@RestController
@CrossOrigin
@RequestMapping(value = "hashrate", produces = "application/json;charset=UTF-8")
public class HashrateController {

    private final static List list = new ArrayList<String>() {
        {
            add("connectWallet");
            add("loginGame");
            add("buyBox");
            add("downloadGame");
        }
    };

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserService userService;


    @RequestMapping("general")
    public Object setGeneral(@RequestBody GeneralFormatVo generalFormatVo) {
        if (!list.contains(generalFormatVo.getAction())) {
            return false;
        }
        //过期时间代表一天
        redisUtils.set(generalFormatVo.getAction() + "-" + generalFormatVo.getAddress()
                , JSONObject.toJSONString(generalFormatVo), 24 * 60 * 60);
        if (generalFormatVo.getAction() == "downloadGame") {
            userService.updateDownload(generalFormatVo.getAddress().toLowerCase());
        }
        return "";
    }


    //当前用户算力
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

//    @RequestMapping("getALL")
//    public Object getAll() {
//        GeneralFormatVo gen = JSON.parseObject(redisUtils.get("connectWallet-connectWallet-0x1234")
//                , GeneralFormatVo.class);
//        return gen;
//    }


//不加算力只做一个标记
//    @RequestMapping("downloadGame")
//    public Object setDownloadGame() {
//        return "";
//    }


// 不增加算力
//    @RequestMapping("durationGame")
//    public Object setDurationGame() {
//        return "";
//    }


// 暂时没有
//    @RequestMapping("inviteGroup")
//    public Object setInviteGroup() {
//        return "";
//    }


}

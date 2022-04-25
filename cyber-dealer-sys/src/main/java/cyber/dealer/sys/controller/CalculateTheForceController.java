package cyber.dealer.sys.controller;

import com.alibaba.fastjson.JSONObject;
import cyber.dealer.sys.domain.vo.GeneralFormatVo;
import cyber.dealer.sys.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
        }
    };

    @Autowired
    private RedisUtils redisUtils;

    @RequestMapping("general")
    public Object setGeneral(@RequestBody GeneralFormatVo generalFormatVo) {
        if (!list.contains(generalFormatVo.getAction())) {
            return false;
        }
        //过期时间代表一天
        redisUtils.set(generalFormatVo.getAction() + "-" + generalFormatVo.getAddress()
                , JSONObject.toJSONString(generalFormatVo), 24 * 60 * 60);
        return "";
    }
}

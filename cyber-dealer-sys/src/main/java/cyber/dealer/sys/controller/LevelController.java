package cyber.dealer.sys.controller;

import cyber.dealer.sys.domain.CyberUsers;
import cyber.dealer.sys.service.CyberUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static cyber.dealer.sys.util.Common.decorateReturnObject;

/**
 * @author lfy
 * @Date 2022/4/22 17:18
 */
@RestController
@RequestMapping("level")
public class LevelController {

    @Autowired
    private CyberUsersService cyberUsersService;


    //注册最大级别 全球级
    @PostMapping("inviter")
    public Object inviter(@RequestParam String addr) {
        return decorateReturnObject(cyberUsersService.inviter(addr));
    }

    //其他级别生成
    @PostMapping("invitation")
    public Object invitation(@RequestParam String addr, @RequestParam String icode) {
        return decorateReturnObject(cyberUsersService.invitation(addr, icode));
    }


}

package cyber.dealer.sys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cyber.dealer.sys.constant.ReturnObject;
import cyber.dealer.sys.domain.CyberUsers;
import cyber.dealer.sys.mapper.CyberUsersMapper;
import cyber.dealer.sys.service.CyberUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static cyber.dealer.sys.util.Common.decorateReturnObject;

/**
 * @author lfy
 * @Date 2022/4/22 16:59
 */
@CrossOrigin
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private CyberUsersService cyberUsersService;


    @SaCheckLogin
    @GetMapping("getdata")
    public Object getData(String address) {
        return decorateReturnObject(cyberUsersService.getData(address));
    }


    // 测试登录，浏览器访问： http://localhost:8081/user/doLogin?username=zhang&password=123456
    @GetMapping("doLogin")
    public Object doLogin(String address) {
        return decorateReturnObject(cyberUsersService.eqAddress(address));
    }

    @SaCheckLogin
    @GetMapping("outLogin")
    public Object outLogin(String address){
        return decorateReturnObject(cyberUsersService.outAddress(address));
    }

    //下载埋点
    @GetMapping("download")
    public Object download(String address) {
        return decorateReturnObject(new ReturnObject<>(cyberUsersService.lambdaUpdate().set(CyberUsers::getDownload, true).eq(CyberUsers::getAddress, address).update()));
    }


    @PostMapping("isLogin")
    public String isLogin() {
        return "当前会话是否登录：" + StpUtil.isLogin() + "" + StpUtil.getLoginId();
    }


}

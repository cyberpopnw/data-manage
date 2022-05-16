package cyber.dealer.sys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlLike;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cyber.dealer.sys.constant.ReturnObject;
import cyber.dealer.sys.domain.CyberUsers;
import cyber.dealer.sys.exception.ExceptionCast;
import cyber.dealer.sys.service.CyberUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.Keys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cyber.dealer.sys.constant.ReturnNo.AUTH_INVALID_EQADMINEX;
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
//    @SaCheckPermission("user-get")
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
    public Object outLogin(String address) {
        return decorateReturnObject(cyberUsersService.outAddress(address));
    }

    //下载埋点
    @GetMapping("download")
    public Object download(String address) {
        return decorateReturnObject(new ReturnObject<>(cyberUsersService.lambdaUpdate().set(CyberUsers::getDownload, true).eq(CyberUsers::getAddress, address).update()));
    }


    @PostMapping("isLogin")
    public Object isLogin() {
        return "当前会话是否登录：" + StpUtil.isLogin() + "" + StpUtil.getLoginId();
    }

    @SaCheckLogin
//    @SaCheckPermission("super-admin")
    @PostMapping("disable")
    public Object isLogin(String address, Integer day) {
//        CyberUsers entity = cyberUsersService.lambdaQuery().eq(CyberUsers::getAddress, address).getEntity();
        LambdaQueryWrapper<CyberUsers> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CyberUsers::getAddress, address);
        CyberUsers one = cyberUsersService.getOne(queryWrapper);
        long time = 0;
        if (day == -1) {
            time = -1;
        } else if (day > 0) {
            time = day * 60 * 60 * 24;
        } else {
            ExceptionCast.cast(AUTH_INVALID_EQADMINEX);
        }
        StpUtil.kickout(one.getId());
        StpUtil.disable(one.getId(), time);
        return "当前会话是否登录：" + StpUtil.isLogin() + "" + StpUtil.getLoginId();
    }

    @SaCheckLogin
//    @SaCheckPermission("user-get")
    @PostMapping("getdatas")
    public Object getDatas(String address) {
        address = Keys.toChecksumAddress(address);
        return decorateReturnObject(cyberUsersService.findAll(address));
    }

    @GetMapping("getuser")
    public Object getUser(String address) {
        address = Keys.toChecksumAddress(address);
//        cyberUsersService.lambdaQuery().eq(CyberUsers::getAddress, address).getEntity();
        QueryWrapper<CyberUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("address", address);
        CyberUsers one = cyberUsersService.getOne(queryWrapper);
        return decorateReturnObject(new ReturnObject<>(one));
    }

    @PutMapping("nickname")
    public Object setNickname(String nikename, String address) {
        address = Keys.toChecksumAddress(address);
        return decorateReturnObject(new ReturnObject<>(cyberUsersService.setNikename(nikename, address)));
    }

    //是否有这个邮箱 /这个邮箱绑定了什么address
    @GetMapping("bemail")
    public Object bEmail(String email) {
        LambdaQueryWrapper<CyberUsers> ambdaQueryWrap = new LambdaQueryWrapper<>();
        ambdaQueryWrap.eq(CyberUsers::getEmail, email);
        System.out.println(email);
        CyberUsers entity = cyberUsersService.getOne(ambdaQueryWrap);
        System.out.println(entity);
        if (entity == null) {
            return decorateReturnObject(new ReturnObject<>(true));
        }
        return decorateReturnObject(new ReturnObject<>(entity.getAddress()));
    }

    @GetMapping("baddress")
    public Object bAddress(String address) {
        address = Keys.toChecksumAddress(address);
        LambdaQueryWrapper<CyberUsers> ambdaQueryWrap = new LambdaQueryWrapper<>();
        ambdaQueryWrap.eq(CyberUsers::getAddress, address);
        System.out.println(address);
        CyberUsers entity = cyberUsersService.getOne(ambdaQueryWrap);
        if (entity == null) {
            return decorateReturnObject(new ReturnObject<>(true));
        }
        return decorateReturnObject(new ReturnObject<>(entity.getEmail()));
    }

    @GetMapping("fselect")
    public Object fselect(String address, String email, Integer page, Integer pageSize) {
//        address = Keys.toChecksumAddress(address);

        if ("".equals(address) && "".equals(email)) {
            return decorateReturnObject(new ReturnObject<>(false));
        }

        IPage<CyberUsers> pages = new Page<>(page, pageSize);
        LambdaQueryWrapper<CyberUsers> lambdaQueryWrap = new LambdaQueryWrapper<>();
        lambdaQueryWrap
                .like(!"".equals(address), CyberUsers::getAddress, address)
                .like(!"".equals(email), CyberUsers::getEmail, email)
        ;

        IPage<CyberUsers> page1 = cyberUsersService.page(pages, lambdaQueryWrap);

        long pages1 = page1.getPages();
        List records = page1.getRecords();
        long total = page1.getTotal();
        long size = page1.getSize();
        if (records == null) {
            return decorateReturnObject(new ReturnObject<>(false));
        }
        Map map = new HashMap();
        map.put("page", pages1);
        map.put("total", total);
        map.put("size", size);
        records.add(map);

        return decorateReturnObject(new ReturnObject<>(records));
    }
}

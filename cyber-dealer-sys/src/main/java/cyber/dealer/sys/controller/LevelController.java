package cyber.dealer.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cyber.dealer.sys.constant.ReturnObject;
import cyber.dealer.sys.domain.CyberAgency;
import cyber.dealer.sys.domain.CyberUsers;
import cyber.dealer.sys.exception.ExceptionCast;
import cyber.dealer.sys.service.CyberAgencyService;
import cyber.dealer.sys.service.CyberUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.Keys;

import java.util.HashMap;
import java.util.Map;

import static cyber.dealer.sys.constant.ReturnNo.AUTH_INVALID_CODE;
import static cyber.dealer.sys.util.Common.decorateReturnObject;

/**
 * @author lfy
 * @Date 2022/4/22 17:18
 */
@RestController
@RequestMapping("level")
@CrossOrigin
public class LevelController {

    @Autowired
    private CyberUsersService cyberUsersService;

    @Autowired
    private CyberAgencyService cyberAgencyService;


    //其他级别生成
    @PostMapping("invitation")
    public Object invitation(
            @RequestParam(required = true) String addr,
            @RequestParam(value="icode",required = false, defaultValue = "8BvDNz") String icode,
//            @RequestParam(value="icode",required = false, defaultValue = "uzG7x5") String icode,
            @RequestParam(required = true) String email,
            @RequestParam(required = true) String nikename) {
        addr = Keys.toChecksumAddress(addr);
        return decorateReturnObject(cyberUsersService.invitation(addr, icode, email, nikename));
    }


    @GetMapping("eqaddr")
    public Object eqAddr(String addr) {
        addr = Keys.toChecksumAddress(addr);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("address", addr);
        CyberUsers entity = cyberUsersService.getOne(queryWrapper);
        Map map = new HashMap();
        if (entity == null) {
            return decorateReturnObject(new ReturnObject<>(true));
        }
        if (entity.getInvId() == 0) {
            map.put("level", entity.getLevel());
            return decorateReturnObject(new ReturnObject<>(map));
        }
        QueryWrapper queryWrappers = new QueryWrapper<>();
        queryWrappers.eq("id", entity.getInvId());
        CyberAgency one = cyberAgencyService.getOne(queryWrappers);
        if (one == null) {
            ExceptionCast.cast(AUTH_INVALID_CODE);
        }
        String Inv_level = null;
        if (entity.getLevel() == 1) {
            Inv_level = one.getThreeClass();
        }
        if (entity.getLevel() == 2) {
            Inv_level = one.getTwoClass();
        }
        if (entity.getLevel() == 3) {
            Inv_level = one.getOneClass();
        }
        if (Inv_level == null) {
            ExceptionCast.cast(AUTH_INVALID_CODE);
        }
        map.put("inv_level", Inv_level);
        map.put("level", entity.getLevel());
        map.put("data", false);
        return decorateReturnObject(new ReturnObject<>(map));
    }


}

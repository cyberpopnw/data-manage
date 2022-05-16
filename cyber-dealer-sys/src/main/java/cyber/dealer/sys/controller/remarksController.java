package cyber.dealer.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import cyber.dealer.sys.constant.ReturnObject;
import cyber.dealer.sys.domain.CyberUsers;
import cyber.dealer.sys.domain.CyberUsersRemarks;
import cyber.dealer.sys.exception.ExceptionCast;
import cyber.dealer.sys.mapper.CyberUsersMapper;
import cyber.dealer.sys.mapper.CyberUsersRemarksMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.Keys;

import javax.management.Query;

import java.util.HashMap;
import java.util.Map;

import static cyber.dealer.sys.constant.ReturnNo.AUTH_INVALID_EQADMINEX;
import static cyber.dealer.sys.constant.ReturnNo.FIELD_NOTVALID;
import static cyber.dealer.sys.util.Common.decorateReturnObject;

/**
 * @author lfy
 * @Date 2022/5/5 20:14
 */
@RestController
@RequestMapping("re")
@CrossOrigin
public class remarksController {

    @Autowired
    private CyberUsersRemarksMapper cyberUsersRemarksMapper;


    @PutMapping("setremarks")
    public Object setRemarks(String address, String toaddress, String remarks) {

        if (address == null) {
            ExceptionCast.cast(FIELD_NOTVALID);
        }
        if (remarks == null) {
            ExceptionCast.cast(FIELD_NOTVALID);
        }
        if (toaddress == null) {
            ExceptionCast.cast(FIELD_NOTVALID);
        }
        address = Keys.toChecksumAddress(address);
        toaddress = Keys.toChecksumAddress(toaddress);

        CyberUsersRemarks cyberUsersRemarks = new CyberUsersRemarks();
        cyberUsersRemarks.setRemarks(remarks);
        cyberUsersRemarks.setAddress(address);
        cyberUsersRemarks.setToaddress(toaddress);

        QueryWrapper queryWrapper = new QueryWrapper();
        Map<String, Object> map = new HashMap<>();
        map.put("address", address);
        map.put("toaddress", toaddress);
        queryWrapper.allEq(map);
        CyberUsersRemarks cyberUsersRemarks1 = cyberUsersRemarksMapper.selectOne(queryWrapper);
        if (cyberUsersRemarks1 == null) {
            return decorateReturnObject(new ReturnObject<>(cyberUsersRemarksMapper.insert(cyberUsersRemarks) == 1));
        }
        UpdateWrapper<CyberUsersRemarks> remarksR = new UpdateWrapper<>();
        remarksR.eq("address", address);
        remarksR.eq("toaddress", toaddress);
        remarksR.set("remarks", remarks);
        return decorateReturnObject(new ReturnObject<>(cyberUsersRemarksMapper.update(cyberUsersRemarks, remarksR) == 1));
    }
}

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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private CyberUsersMapper cyberUsersMapper;

    @Autowired
    private CyberUsersRemarksMapper cyberUsersRemarksMapper;


    @GetMapping("setremarks")
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
            int insert = cyberUsersRemarksMapper.insert(cyberUsersRemarks);
            return decorateReturnObject(new ReturnObject<>(insert == 0 ? false : true));
        }
        UpdateWrapper<CyberUsersRemarks> remarksR = new UpdateWrapper<>();
        remarksR.eq("address", address);
        remarksR.eq("toaddress", toaddress);
        remarksR.set("remarks", remarks);
        int update = cyberUsersRemarksMapper.update(cyberUsersRemarks, remarksR);
        return decorateReturnObject(new ReturnObject<>(update == 0 ? false : true));
    }
}

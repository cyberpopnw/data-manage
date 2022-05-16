package cyber.dealer.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cyber.dealer.sys.constant.ReturnObject;
import cyber.dealer.sys.domain.CyberAgency;
import cyber.dealer.sys.domain.CyberUsers;
import cyber.dealer.sys.mapper.CyberAgencyMapper;
import cyber.dealer.sys.mapper.CyberUsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.Keys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cyber.dealer.sys.util.Common.decorateReturnObject;

/**
 * @author lfy
 * @Date 2022/5/5 10:39
 */
@RestController
@RequestMapping("BF")
@CrossOrigin
public class BCFindAllController {

    @Autowired
    private CyberUsersMapper cyberUsersMapper;

    @Autowired
    private CyberAgencyMapper cyberAgencyMapper;


    @GetMapping("findor")
    public Object findRegion(String address) {
        address = Keys.toChecksumAddress(address);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("address", address);
        CyberUsers cyberUsers = cyberUsersMapper.selectOne(queryWrapper);

        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("uid", cyberUsers.getId());
        CyberAgency cyberAgency = cyberAgencyMapper.selectOne(queryWrapper1);
        if (cyberAgency == null) {
            return decorateReturnObject(new ReturnObject<>(false));
        }
        QueryWrapper queryWrapper2 = new QueryWrapper();
        queryWrapper2.eq("inv_id", cyberAgency.getId());
        queryWrapper2.orderByDesc("level");
        List list = cyberUsersMapper.selectList(queryWrapper2);
        return decorateReturnObject(new ReturnObject<>(list));
    }

    @GetMapping("findalldatauser")
    public Object getFindAllDataUser(Integer level, Integer page, Integer pageSize) {
        QueryWrapper<CyberUsers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("level", level);
        queryWrapper.eq("inv_id", 0);
        IPage<CyberUsers> pages = new Page<>(page, pageSize);
        IPage<CyberUsers> cyberUsersIPage = cyberUsersMapper.selectPage(pages, queryWrapper);
        List cyberUserss = cyberUsersIPage.getRecords();
        long pages1 = cyberUsersIPage.getPages();
        long total = cyberUsersIPage.getTotal();
        long current = cyberUsersIPage.getCurrent();
        if (!cyberUserss.isEmpty()) {
            Map<String, Object> map = new HashMap<>();
            map.put("pages", pages1);
            map.put("total", total);
            map.put("current", current);
            cyberUserss.add(map);
            return decorateReturnObject(new ReturnObject<>(cyberUserss));
        }
        return decorateReturnObject(new ReturnObject<>(false));
    }


    @GetMapping("findAlldata")
    public Object findAlldata() {
        //国家级人数
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1
                .eq("level", 4);
        Integer level4count = cyberUsersMapper.selectCount(queryWrapper1);
        //区域人数
        QueryWrapper queryWrapper2 = new QueryWrapper();
        queryWrapper2
                .eq("level", 3);
        Integer level3count = cyberUsersMapper.selectCount(queryWrapper2);
        //伙伴人数
        QueryWrapper queryWrapper3 = new QueryWrapper();
        queryWrapper3
                .eq("level", 2);
        Integer level2count = cyberUsersMapper.selectCount(queryWrapper3);
        //用户
        QueryWrapper queryWrapper4 = new QueryWrapper();
        queryWrapper4
                .eq("level", 1);
        Integer level1count = cyberUsersMapper.selectCount(queryWrapper4);

        Map map = new HashMap();
        map.put("level4count", level4count);
        map.put("level3count", level3count);
        map.put("level2count", level2count);
        map.put("level1count", level1count);

        return decorateReturnObject(new ReturnObject<>(map));
    }
}

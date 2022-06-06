package cyber.dealer.sys.controller;

import cyber.dealer.sys.mapper.CyberUsersRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lfy
 * @Date 2022/5/7 14:32
 */
@RestController
@RequestMapping("record")
@CrossOrigin
public class CyberUsersRecordController {

    @Autowired
    private CyberUsersRecordMapper cyberUsersRecordMapper;

    @GetMapping("getdata")
    public Object getRecordData(String address,Integer level){
        
//        cyberUsersRecordMapper
        return "";
    }

}

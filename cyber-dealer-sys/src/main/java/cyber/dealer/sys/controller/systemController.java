package cyber.dealer.sys.controller;

import cyber.dealer.sys.constant.ReturnObject;
import cyber.dealer.sys.exception.ExceptionCast;
import cyber.dealer.sys.service.CyberDealersSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static cyber.dealer.sys.constant.ReturnNo.FIELD_NOTVALID;
import static cyber.dealer.sys.util.Common.decorateReturnObject;

/**
 * @author lfy
 * @Date 2022/5/12 10:28
 */
@CrossOrigin
@RestController
@RequestMapping("sys")
public class systemController {

    @Autowired
    private CyberDealersSystemService cyberDealersSystemService;

    //设置奖励池
    @PutMapping("setreward")
    public Object setReward(Integer reward) {
        if (reward < 0) {
            ExceptionCast.cast(FIELD_NOTVALID);
        }
        return decorateReturnObject(new ReturnObject<>(cyberDealersSystemService.setReward(reward)));
    }

    @PutMapping("commission")
    public Object setcommission(Integer level, Double commission) {
        if (level < 0 && commission <= 1) {
            ExceptionCast.cast(FIELD_NOTVALID);
        }
        return decorateReturnObject(new ReturnObject<>(cyberDealersSystemService.setcommission(level,commission)));
    }


    @GetMapping("commission")
    public Object getcommission(Integer level) {
        if (level != 3 && level != 2 && level != 4) {
            ExceptionCast.cast(FIELD_NOTVALID);
        }
        return decorateReturnObject(new ReturnObject<>(cyberDealersSystemService.getcommission(level)));
    }

}

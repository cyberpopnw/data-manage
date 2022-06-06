package cyber.dealer.sys.controller;

import cn.dev33.satoken.stp.StpUtil;
import cyber.dealer.sys.constant.ReturnObject;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.Keys;

import java.util.Arrays;
import java.util.List;

import static cyber.dealer.sys.util.Common.decorateReturnObject;

/**
 * @author lfy
 * @Date 2022/5/20 10:17
 */
@RestController
@RequestMapping("separate")
@CrossOrigin
public class SeparatePageController {

    private static final List<String> list = Arrays.asList(
            "0x76110C4d5c5a7Fe4Db304e35593490822701F484",//老板
            "0x7291030263771b40731D6Bc6b352358D23F5737F",//老板
            "0xDd2fbdB1DCC92a3EAeB75d87d839BEb9A1b64131",//老板
            "0xF5B06D48eDC1d6fE1091efF98f7EE63d204385A0",//老板
            "0x495f52923039Cd6D0F48616B7a1a930AaD3f815F",//老板
            "0xf7fB89554f842F550499AEf4FDa2d1898039851f",//我的
            "0x01F137d7B8959ab1560A92227d4554572e5116c0"//周城
    );


    @GetMapping("dologin")
    public Object dologin(@RequestParam String address) {
        if (address != null) {
            address = Keys.toChecksumAddress(address);
            if (list.contains(address)) {
                StpUtil.login(address);
                return decorateReturnObject(new ReturnObject<>(true));
            }
        }
        return decorateReturnObject(new ReturnObject<>(false));
    }

    @GetMapping("outlogin")
    public Object outlogin(@RequestParam String address) {
        address = Keys.toChecksumAddress(address);
        StpUtil.logout(address);
        return decorateReturnObject(new ReturnObject<>(true));
    }

}

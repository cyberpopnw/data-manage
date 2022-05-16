package cyber.dealer.sys.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletUtils;

/**
 * @author lfy
 * @Date 2022/5/11 16:33
 */
@RestController
@CrossOrigin
@RequestMapping("test")
public class testController {

    @GetMapping("address")
    public String isAddress(String address) {
        boolean validAddress = WalletUtils.isValidAddress(address);
        String s = Keys.toChecksumAddress(address);
        return s;
    }
}

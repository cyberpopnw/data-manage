package cyber.dealer.sys;

import cn.dev33.satoken.SaManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.web3j.crypto.WalletUtils;

@SpringBootApplication
public class CyberDealerSysApplication {

    public static void main(String[] args) {
        SpringApplication.run(CyberDealerSysApplication.class, args);
        System.out.println("启动成功：Sa-Token配置如下：" + SaManager.getConfig());
    }

}

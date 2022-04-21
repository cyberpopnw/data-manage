package com.bastion.cyber.take;

import com.alibaba.fastjson.JSON;
import com.bastion.cyber.model.po.UserPo;
import com.bastion.cyber.service.UserService;
import com.bastion.cyber.utils.HttpURLConnectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;

/**
 * @author lfy
 * @Date 2022/4/21 11:41
 */
@Configuration
@EnableScheduling
public class BalanceTake {


    @Autowired
    private UserService userService;

    //定义一个按一定频率执行的定时任务，每隔1*60分钟执行一次，延迟1秒执行
    @Scheduled(fixedRate = 1000 * 60 * 60, initialDelay = 1000)
    public void synchronousCoin() {
        List<UserPo> list = userService.findAllAddress();

        for (int i = 0; i < list.size(); i++) {
            String mumbaiUrl = "https://testquery.cyberpop.online/tokensOfOwner?chainId=43113&contractAddress=0x78F66E37e9fE077d2F0126E3a26e6FB0D14F2BB0&accounts=" + list.get(i).getAddr();
            String fujiUrl = "https://testquery.cyberpop.online/tokensOfOwner?chainId=80001&contractAddress=0x37e769d34Cb48fb074fDd181bB4d803fBD49C712&accounts=" + list.get(i).getAddr();
            System.out.println(mumbaiUrl);
            String mumbaiQ = HttpURLConnectionUtil.doGet(mumbaiUrl);
            String fujiQ = HttpURLConnectionUtil.doGet(fujiUrl);
            Map mumbaimap = JSON.parseObject(mumbaiQ, Map.class);
            Map fujimap = JSON.parseObject(fujiQ, Map.class);
            List mumbaidata = (List) mumbaimap.get("data");
            List fujidata = (List) fujimap.get("data");
            userService.saveCoin(mumbaidata.size(), fujidata.size(), String.valueOf(list.get(i).getAddr()));
        }
    }

}

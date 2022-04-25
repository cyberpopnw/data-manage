package cyber.dealer.sys.service;

import cyber.dealer.sys.constant.ReturnObject;
import cyber.dealer.sys.domain.CyberUsers;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author lfy
* @description 针对表【cyber_users】的数据库操作Service
* @createDate 2022-04-22 16:33:47
*/
public interface CyberUsersService extends IService<CyberUsers> {

    ReturnObject<Object> inviter(String addr);

    ReturnObject<Object> invitation(String addr, String icode);

    ReturnObject<Object> getData(String addr);

    ReturnObject<Object> eqAddress(String addr);

    ReturnObject<Object> outAddress(String address);
}

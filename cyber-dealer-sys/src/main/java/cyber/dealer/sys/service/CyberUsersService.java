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


    ReturnObject<Object> invitation(String addr, String icode,String email, String nickname);

    ReturnObject<Object> getData(String addr);

    ReturnObject<Object> eqAddress(String addr);

    ReturnObject<Object> outAddress(String address);

    ReturnObject<Object> findAll(String address);

    Object setNikename(String nikename, String address);
}

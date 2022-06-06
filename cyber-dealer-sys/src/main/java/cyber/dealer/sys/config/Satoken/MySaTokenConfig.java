package cyber.dealer.sys.config.Satoken;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.interceptor.SaAnnotationInterceptor;
import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import cn.dev33.satoken.strategy.SaStrategy;
import cn.dev33.satoken.util.SaFoxUtil;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author lfy
 * @Date 2022/4/25 11:06
 */
@Configuration
public class MySaTokenConfig implements WebMvcConfigurer {
    // 注册sa-token的登录拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册登录拦截器，并排除登录接口地址
        registry.addInterceptor(new SaRouteInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/user/doLogin"
                        , "/user/bemail"
                        , "/user/baddress"
                        , "/level/invitation"
                        , "/business/nationallevel"
                        , "/business/deluserlevel"
                        , "/business/arealevel"
                        , "/business/partnerlevel"
                        , "/business/userlevel"
                        , "/connection/general"
                        , "/re/setremarks"
                        , "/user/outLogin"
                        , "/user/download"
                        , "/user/isLogin"
                        , "/business/invuser"
                        , "/user/getuser"
                        , "/separate/dologin"
                        ,"/level/eqaddr"
                )
        ;
    }
}

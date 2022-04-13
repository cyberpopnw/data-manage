package com.bastion.cyber.annotation.aspect;

import com.bastion.cyber.annotation.aop.BastLogger;
import com.bastion.cyber.model.bo.AuthBo;
import com.bastion.cyber.model.po.CyberBuriedPointPo;
import com.bastion.cyber.model.vo.AccessTokenVo;
import com.bastion.cyber.service.CyberBuriedPointService;
import com.bastion.cyber.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static com.bastion.cyber.constant.Global.LOGIN_TOKEN_KEY;
import static com.bastion.cyber.utils.JwtHelper.verifyTokenAndGetClaims;

@Aspect
@Component
@Slf4j
public class LoggerAspect {

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final CyberBuriedPointService cyberBuriedPointService;

    public LoggerAspect(ThreadPoolTaskExecutor threadPoolTaskExecutor, CyberBuriedPointService cyberBuriedPointService) {
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.cyberBuriedPointService = cyberBuriedPointService;
    }

    @Pointcut("@annotation(com.bastion.cyber.annotation.aop.BastLogger)")
    public void pointcut() {

    }

    @Around(value = "pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {

        //先执行业务
        Object result = joinPoint.proceed();
        try {
            // 日志收集
            handle(joinPoint, result);
        } catch (Exception e) {
            log.error("日志记录出错!", e);
        }

        return result;
    }

    /**
     * 管理员日志收集
     */
    private void handle(ProceedingJoinPoint joinPoint, Object result) {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Object[] vals = joinPoint.getArgs();
        Method method = ms.getMethod();
        BastLogger annotation = method.getAnnotation(BastLogger.class);
        if (Objects.isNull(annotation)) {
            return;
        }

        String name = method.toString();
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String token;
        if (name.contains("controller.UserController.auth") || name.contains("controller.UserController.report")) {
            Object data = ((HashMap) result).get("data");
            if (data instanceof AccessTokenVo) {
                token = ((AccessTokenVo) data).getAccessToken();
            } else {
                log.error("校验参数返回异常");
                return;
            }
        } else {
            token = request.getHeader(LOGIN_TOKEN_KEY);
        }

        String requestURI = request.getRequestURI();
        String type = request.getMethod();
        String ip = IpUtils.getIpAddr(request);
        String cityInfo = IpUtils.getCityInfo(ip);
        AuthBo authBo = verifyTokenAndGetClaims(token);


        Parameter[] parameters = method.getParameters();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parameters.length; i++) {
            Object val = vals[i];
            Method stringMethod = ReflectionUtils.findMethod(val.getClass(), "string");
            Object valObj;
            if (Objects.isNull(stringMethod)) {
                valObj = val;
            } else {
                valObj = ReflectionUtils.invokeMethod(stringMethod, val);
            }
            sb.append(parameters[i].getName()).append("=").append(valObj).append(",");
        }
        sb = new StringBuilder(sb.substring(0, sb.length() - 1));
        String params = sb.toString();

        CyberBuriedPointPo pointPo = CyberBuriedPointPo.builder()
                .uid(authBo.getId())
                .ip(ip)
                .url(requestURI)
                .method(type)
                .params(params)
                .ipSource(cityInfo)
                .build();

        log.debug("{} - {}", ip, requestURI);

        // 异步存储日志
        CompletableFuture.runAsync(() -> cyberBuriedPointService.insert(pointPo), threadPoolTaskExecutor);
    }
}

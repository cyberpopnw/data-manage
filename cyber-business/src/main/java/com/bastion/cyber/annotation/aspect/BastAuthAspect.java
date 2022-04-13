package com.bastion.cyber.annotation.aspect;

import com.bastion.cyber.annotation.aop.BastAuthBo;
import com.bastion.cyber.model.bo.AuthBo;
import com.bastion.cyber.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static com.bastion.cyber.constant.Global.LOGIN_TOKEN_KEY;
import static com.bastion.cyber.constant.ReturnNo.AUTH_NEED_LOGIN;
import static com.bastion.cyber.utils.JwtHelper.verifyTokenAndGetClaims;
import static java.util.Objects.requireNonNull;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.web.context.request.RequestContextHolder.getRequestAttributes;

@Slf4j
@Aspect
@Component
public class BastAuthAspect {
    @Pointcut("@annotation(com.bastion.cyber.annotation.aop.BastAuth)")
    public void bastringAspect() {
    }

    @Around("bastringAspect()")
    public Object around(ProceedingJoinPoint joinPoint) {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();

        HttpServletRequest request = ((ServletRequestAttributes) requireNonNull(getRequestAttributes())).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) getRequestAttributes()).getResponse();
        String token = request.getHeader(LOGIN_TOKEN_KEY);
        if (token == null) {
            requireNonNull(response).setStatus(SC_UNAUTHORIZED);
            return ResponseUtil.fail(AUTH_NEED_LOGIN);
        }

        AuthBo authBo = verifyTokenAndGetClaims(token);
        if (Objects.isNull(authBo)) {
            requireNonNull(response).setStatus(SC_UNAUTHORIZED);
            log.warn("around userId is null");
            return ResponseUtil.fail(AUTH_NEED_LOGIN);
        }
        log.debug("around: AuthUser = {}", authBo.string());

        Object[] args = joinPoint.getArgs();
        Annotation[][] annotations = method.getParameterAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            Annotation[] paramAnn = annotations[i];
            if (paramAnn.length == 0) {
                continue;
            }

            for (Annotation annotation : paramAnn) {
                if (annotation.annotationType().equals(BastAuthBo.class)) {
                    AuthBo.AuthBoBuilder arg1 = AuthBo.builder();
                    Arrays.stream(Optional.of((BastAuthBo) annotation)
                                    .map(BastAuthBo::value).orElseGet(() -> new String[]{}))
                            .forEach(field -> {
                                switch (field) {
                                    case "id":
                                        arg1.id(authBo.getId());
                                        break;
                                    default:
                                        break;
                                }
                            });
                    args[i] = arg1.build();
                }
            }
        }

        Object obj = null;
        try {
            obj = joinPoint.proceed(args);
        } catch (Throwable e) {

        }
        return obj;
    }
}

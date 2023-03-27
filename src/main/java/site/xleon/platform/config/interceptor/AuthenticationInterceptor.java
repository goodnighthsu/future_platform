package site.xleon.platform.config.interceptor;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import site.xleon.platform.config.DefaultExceptionHandler;
import site.xleon.platform.core.JWT;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

/**
 * 接口鉴权
 */
@Component
@AllArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final JWT jwt;

    @Override
    public boolean preHandle(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod method = (HandlerMethod) handler;
        if (method.getBeanType().isAssignableFrom(DefaultExceptionHandler.class)) {
            return true;
        }

        jwt.getUserId(request);

        return true;
    }
}

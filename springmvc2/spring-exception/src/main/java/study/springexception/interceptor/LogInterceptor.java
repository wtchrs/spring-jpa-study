package study.springexception.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    public static final String LOG_ID_ATTR_NAME = "logId";

    /**
     * @param handler RequestMapping: {@link org.springframework.web.method.HandlerMethod}<br>
     *                Static resource: {@link org.springframework.web.servlet.resource.ResourceHttpRequestHandler}
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String requestURI = request.getRequestURI();
        String logId = UUID.randomUUID().toString();
        request.setAttribute(LOG_ID_ATTR_NAME, logId);

        log.info("LogInterceptor.preHandle [{}][{}][{}][{}]", logId, request.getDispatcherType(), requestURI, handler);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        String logId = (String) request.getAttribute(LOG_ID_ATTR_NAME);
        log.info("LogInterceptor.postHandle [{}][{}][{}]", logId, request.getDispatcherType(), modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        String logId = (String) request.getAttribute(LOG_ID_ATTR_NAME);
        String requestURI = request.getRequestURI();
        log.info("LogInterceptor.afterCompletion [{}][{}][{}]", logId, request.getDispatcherType(), requestURI);

        if (ex != null) {
            log.error("LogInterceptor.afterCompletion: Exception occurs at handler", ex);
        }
    }
}

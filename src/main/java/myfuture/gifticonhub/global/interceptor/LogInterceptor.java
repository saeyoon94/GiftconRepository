package myfuture.gifticonhub.global.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    public static final String REQUEST_ID = "requestId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();
        request.setAttribute(REQUEST_ID, uuid);

        log.info("REQUEST START [{}][{}][{}]", uuid, requestURI, handler);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String uuid = (String) request.getAttribute(REQUEST_ID);
        String requestURI = request.getRequestURI();
        log.info("ModelAndView [{}][{}][{}]", uuid, requestURI, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String uuid = (String) request.getAttribute(REQUEST_ID);
        String requestURI = request.getRequestURI();
        log.info("REQUEST END [{}][{}]", uuid, requestURI);

        if (ex != null) {
            log.error("Handler Throws Exceptions!", ex);
        }
    }
}

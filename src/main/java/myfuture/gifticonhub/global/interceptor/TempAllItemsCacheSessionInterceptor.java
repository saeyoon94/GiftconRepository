package myfuture.gifticonhub.global.interceptor;

import lombok.extern.slf4j.Slf4j;
import myfuture.gifticonhub.global.session.SessionConst;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class TempAllItemsCacheSessionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            //메인화면의 Item List
            log.info("All Items = {}", session.getAttribute(SessionConst.ALL_ITEMS));
            session.removeAttribute(SessionConst.ALL_ITEMS);
        }
        return true;
    }
}

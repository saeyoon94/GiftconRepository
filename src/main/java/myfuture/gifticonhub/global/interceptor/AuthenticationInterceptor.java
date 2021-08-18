package myfuture.gifticonhub.global.interceptor;

import lombok.extern.slf4j.Slf4j;
import myfuture.gifticonhub.global.session.SessionConst;
import myfuture.gifticonhub.global.session.SessionDto;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {
    public static final String REQUEST_ID = "requestId";
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uuid = (String) request.getAttribute(REQUEST_ID);
        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            log.info("An unauthenticated user has attempted to access personalized resources! REQUEST [{}][{}]",uuid, requestURI);
            response.sendRedirect("/login"); //handler가 호출되지 않아도 response를 보낼 수 있구나..
            return false;
        }
        SessionDto sessionDto = (SessionDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
        log.info("Authenticated REQUEST [{}][{}], SESSION [{}][{}]",uuid, requestURI, session.getId(), sessionDto.getUserId());

        return true;
    }
}

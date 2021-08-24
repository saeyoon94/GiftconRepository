package myfuture.gifticonhub.global.interceptor;


import lombok.extern.slf4j.Slf4j;
import myfuture.gifticonhub.global.session.SessionConst;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * ItemController에서 itemDetails와 editForm에서 view에 전달할 모델에 담는 값이 동일하다.
 * 그래서 itemDetails에서 model에 담은 값을 editForm에 넘겨주어 DB에 불필요하게 조회 쿼리를 중복해서 날리지 않도록 한다.
 * 이렇게 값을 넘겨주기 위한 수단으로 세션을 사용. 그런데 세션이 유효한 기간동안 이 값을 계속 들고 있으면 메모리에 부담이므로
 * 다시 다른 컨트롤러가 호출되었을 때 세션에 저장된 값을 삭제하기 위한 인터셉터.
 */
@Slf4j
public class TempSessionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            log.info("Temp_Model = {}", session.getAttribute(SessionConst.TEMP_MODEL));
            session.removeAttribute(SessionConst.TEMP_MODEL);
        }
        return true;
    }
}

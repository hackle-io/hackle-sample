package io.hackle.demo;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

/**
 * 서버, 클라이언트에서 공동으로 사용할 deviceId 를 관리하는 인터셉터.
 * 1. 쿠키에 deviceId가 이미 설정되어 있으면 해당 deviceId를 사용한다. (재방문 유저인 경우)
 * 2. 쿠키에 deviceId가 없는경우 새로 생성한다 (신규 유저)
 * 3. client에서 동일한 deviceId를 사용할 수 있게 deviceId를 쿠키에 굽는다.
 * 4. client에서는 서버에서 쿠키로 구워준 deviceId를 사용한다 (index.html 참고)
 */
@Slf4j
public class HackleDeviceInterceptor implements HandlerInterceptor {

    /**
     * deviceId 를 저장할 쿠키명 (상황에 맞게 변경 가능)
     */
    private static final String DEVICE_ID_COOKIE_NAME = "__device_id";

    /**
     * 쿠키 도메인 (메인 도메인으로 설정)
     * ex) elandmall.co.kr
     */
    private static final String COOKIE_DOMAIN = "elandmall.co.kr";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String deviceId = getDeviceIdFromCooke(request);

        if (deviceId == null) {
            deviceId = createDeviceId();
        }

        setDeviceIdToCookie(deviceId, response);
        HackleDeviceHolder.setDeviceId(deviceId);

        return true;
    }

    private String getDeviceIdFromCooke(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (DEVICE_ID_COOKIE_NAME.equals(cookie.getName())) {
                String deviceId = cookie.getValue();
                log.debug("DeviceId from cookie: {}", deviceId);
                return deviceId;
            }
        }

        return null;
    }

    private String createDeviceId() {
        String deviceId = UUID.randomUUID().toString();
        log.debug("DeviceId created: {}", deviceId);
        return deviceId;
    }

    private void setDeviceIdToCookie(String deviceId, HttpServletResponse response) {
        Cookie cookie = new Cookie(DEVICE_ID_COOKIE_NAME, deviceId);
        cookie.setDomain(COOKIE_DOMAIN);
        cookie.setMaxAge(60 * 60 * 24 * 365);
        response.addCookie(cookie);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HackleDeviceHolder.clear();
    }
}

package io.hackle.demo;

/**
 * Controller, Service 등에서 deviceId 를 쉽게 사용하기 위한 Holder
 *
 * @see HackleDeviceInterceptor 인터셉터에서 deviceId 를 설정해줌
 */
public class HackleDeviceHolder {

    private static final ThreadLocal<String> HOLDER = new ThreadLocal<>();

    public static void setDeviceId(String deviceId) {
        HOLDER.set(deviceId);
    }

    public static String getDeviceId() {
        return HOLDER.get();
    }

    public static void clear() {
        HOLDER.remove();
    }
}

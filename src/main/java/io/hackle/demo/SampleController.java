package io.hackle.demo;

import io.hackle.sdk.HackleClient;
import io.hackle.sdk.common.User;
import io.hackle.sdk.common.decision.Decision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SampleController {

    @Autowired
    private HackleClient hackleClient;

    @GetMapping("/sample")
    public String sample(Model model) {

        String deviceId = HackleDeviceHolder.getDeviceId(); // 인터셉터에서 설정한 deviceId
        User user = User.builder()
            .id(deviceId)
            .deviceId(deviceId)
            .build();
        long experimentKey = 999; // 실험키
        Decision decision = hackleClient.variationDetail(experimentKey, user); // A/B 테스트 분배
        model.addAttribute("variation", decision.getVariation());
        return "index.html";
    }

}

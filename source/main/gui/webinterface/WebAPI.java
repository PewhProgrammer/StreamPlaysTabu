package gui.webinterface;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebAPI {

    @RequestMapping("/")
    public String start() {
        return "setup";
    }

    @RequestMapping("/settings")
    public String settings(String gameMode, String platform, String channel) {
        System.out.println("Channel: " + channel);
        System.out.println("Platform: " + platform);
        System.out.println("Mode: " + gameMode);
        return "registerFFA";
    }
}

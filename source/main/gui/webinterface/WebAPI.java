package gui.webinterface;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebAPI {

    @RequestMapping("/")
    public String settings() {
        return "setup";
    }
}

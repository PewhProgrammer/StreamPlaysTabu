package gui.webinterface;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebAPI {

    @MessageMapping("/startGame")
    @SendTo("/register")
    public void startGame(SetupInformation si) {
        System.out.println("Channel: " + si.getChannel());
        System.out.println("Platform: " + si.getPlatform());
        System.out.println("Mode: " + si.getGameMode());
    }
}

package gui.webinterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@Controller
public class WebAPI {

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/startGame")
    @SendTo("/register")
    public boolean startGame(SetupInformation si) {


        fireGreeting();
        System.out.println("Channel: " + si.getChannel());
        System.out.println("Platform: " + si.getPlatform());
        System.out.println("Mode: " + si.getGameMode());

        return true;
    }

    /**
     * Sends to the server
     */
    public void fireGreeting() {
        this.template.convertAndSend("/localJS/register", "Haha");
    }


}

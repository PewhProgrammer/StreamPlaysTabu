package gui.webinterface;

import gui.webinterface.containers.GameModeContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
public class WebAPI {

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/startGame")
    public void startGame(SetupInformation si) {

        System.out.println("Channel: " + si.getChannel());
        System.out.println("Platform: " + si.getPlatform());
        System.out.println("Mode: " + si.getGameMode());

        send("/register", new GameModeContainer(si.getGameMode()));
    }

    public void send(String path, Object o) {
        this.template.convertAndSend("/localJS" + path, o);
    }
}

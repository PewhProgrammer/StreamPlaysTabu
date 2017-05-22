package gui.webinterface;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class RunInterface {

    public static void main(String[] args) {
        SpringApplication.run(RunInterface.class, args);
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI("http://www.example.com"));
            }
        }catch(URISyntaxException | IOException e){
            e.printStackTrace();
        }
    }
}

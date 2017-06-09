package logic.commands;

import logic.bots.AltTwitchBot;
import model.GameModel;

/**
 * Created by Marc on 05.04.2017.
 */
public class Host extends Command {

    private String host;
    private AltTwitchBot hostBot;
    private boolean unhost;

    public Host(GameModel gm, String ch, String host, AltTwitchBot hostBot) {
        super(gm, ch);
        this.host = host;
        this.hostBot = hostBot;
        unhost = false;
    }

    public Host(GameModel gm, String ch, String host) {
        super(gm, ch);
        this.host = host;
        unhost = true;
    }

    @Override
    public void execute() {
        if (unhost) {
            gameModel.unhost(host);
        } else {
            gameModel.host(host, hostBot);
        }
    }

    @Override
    public boolean validate() {
        if (!unhost) {
            return hostBot != null;
        }

        return true;
    }

    public String getHost() {
        return host;
    }

    @Override
    public boolean equals(Object o) {

        if (!o.getClass().equals(this.getClass())) {
            return false;
        }

        Host h = (Host) o;

        return host.equals(h.getHost());
    }
}

package logic.commands;


import model.GameModel;

public class ChatMessage extends Command {

    private final String sender;
    private final String time;
    private final String content;

    public ChatMessage(GameModel gm, String ch, String sender, String time, String content) {
        super(gm, ch);
        this.content = content;
        this.time = time;
        this.sender = sender;
    }

    @Override
    public void execute() {
        gameModel.getSiteController().sendChatMessage(time, thisChannel, sender, content);
    }

    @Override
    public boolean validate() {
        return true;
    }
}

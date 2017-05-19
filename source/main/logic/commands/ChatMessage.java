package logic.commands;


import model.GameModel;

public class ChatMessage extends Command {

    private final String sender;
    private final String content;

    public ChatMessage(GameModel gm, String ch, String sender, String content) {
        super(gm, ch);
        this.content = content;
        this.sender = sender;
    }

    @Override
    public void execute() {
        gameModel.getSiteController().sendChatMessage(thisChannel, sender, content);
    }

    @Override
    public boolean validate() {
        return true;
    }
}

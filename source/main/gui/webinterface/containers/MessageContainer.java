package gui.webinterface.containers;


public class MessageContainer {

    private final String content;
    private final String time;
    private final String channel;
    private final String sender;

    public MessageContainer(String time, String channel, String sender, String content) {
        this.time = time;
        this.channel = channel;
        this.sender = sender;
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public String getTime() {
        return time;
    }

    public String getChannel() {
        return channel;
    }

    public String getSender() {
        return sender;
    }
}

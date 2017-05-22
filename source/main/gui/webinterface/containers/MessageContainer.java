package gui.webinterface.containers;


import org.json.JSONObject;

public class MessageContainer {

    private final String content;
    private final String channel;
    private final String sender;

    public MessageContainer(String channel, String sender, String content) {
        this.channel = channel;
        this.sender = sender;
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public String getChannel() {
        return channel;
    }

    public String getSender() {
        return sender;
    }

    public JSONObject toJSONObject() {

        JSONObject obj = new JSONObject();

        obj.put("channel", channel);
        obj.put("sender", sender);
        obj.put("content", content);

        return obj;
    }
}

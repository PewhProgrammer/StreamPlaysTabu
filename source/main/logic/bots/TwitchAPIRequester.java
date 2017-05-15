package logic.bots;

import common.Log;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.HashMap;

/**
 * Created by WASABI on 15.05.2017.
 */
public class TwitchAPIRequester {

    private HashMap<String ,String> tokens;

    /* list of requests that need a certain permission
     * https://github.com/justintv/Twitch-API/blob/master/authentication.md
     */

    private enum RequestType {
        GET,
        PUT,
        DELETE
    }

    public TwitchAPIRequester() {
        this.tokens = new HashMap<>();
        parseTokens();
    }

    private void parseTokens() {
        try {
            FileReader fr = new FileReader("data/apitokens.txt");
            BufferedReader br = new BufferedReader(fr);
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(":");
                tokens.put(split[0], split[1]);
                count++;
                if (!validateToken(split[0])) {
                    tokens.remove(split[0]);
                    count--;
                }
            }

            br.close();

            //Log.info("TwitchAPIRequester:parseTokens", count + " token(s) loaded.");
            //Log.writeNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the token is valid.
     * @return true if token is valid.
     */
    private boolean validateToken(String user) {
       // Log.writeDebugMessage("TwitchAPIRequester:validateToken", "Check " + user + "'s token.");
        String response = sendRequest("", RequestType.GET, user);
       // Log.writeNewLine();
        return response.contains("true");
    }

    private String getToken(String user) {
        if (tokens.containsKey(user)) {
            return tokens.get(user);
        }

        throw new IllegalArgumentException("No token for " + user + " available.");
    }

    /**
     * Get channel
     * @param channel Channel to get
     * @return true if channel exists
     */
    public boolean requestChannel(String channel) {
        //Log.writeDebugMessage("TwitchAPIRequester:requestChannel", "Request channel '" + channel + "'.");
        String url = "/channels/" + channel;
        String response = sendRequest(url, RequestType.GET);
        //Log.writeNewLine();
        return !response.contains("status: 404");
    }

    /**
     * Get the list of all channels the user is following
     * @param user The users whose follower list is requested
     * @return the list of all channels the user is following
     */
    public String requestFollowList(String user) {
       // Log.writeDebugMessage("TwitchAPIRequester:requestFollowList", "Request " + user + "'s follow list.");
        String url = "/users/" + user + "/follows/channels";
        String response = sendRequest(url, RequestType.GET);
        //Log.writeNewLine();
        return response;
    }

    /**
     * Adds the user to the targets follower list
     * @param user User who is added to the targets follower list.
     * @param target Target to whose follower list the user is added.
     */
    public void requestFollow(String user, String target) {
       // Log.writeDebugMessage("TwitchAPIRequester:requestFollow", "Request " + user + " to follow " + target + ".");
        String url = "/users/" + user + "/follows/channels/" + target;
        sendRequest(url, RequestType.PUT, user);
       // Log.writeNewLine();
    }

    /**
     * Removes the user from the targets follower list
     * @param user User who is going to removed.
     * @param target Target from whose list the user is removed.
     */
    public void requestUnfollow(String user, String target) {
       // Log.writeDebugMessage("TwitchAPIRequester:requestFollow", "Request " + user + " to unfollow " + target + ".");
        String url = "/users/" + user + "/follows/channels/" + target;
        sendRequest(url, RequestType.DELETE, user);
       // Log.writeNewLine();
    }

    /**
     * Sends a http-request of given type to the given url
     * @param url The URL which is requested.
     * @param requestType The request type of the request
     * @return server callback
     */
    private String sendRequest(String url, RequestType requestType) {
        return sendRequest(url, requestType, "");
    }

    /**
     * Sends a http-request of given type to the given url
     * @param url The URL which is requested.
     * @param requestType The request type of the request
     * @param user The user whose API-authenticationtoken is needed
     * @return server callback
     */
    private String sendRequest(String url, RequestType requestType, String user) {
        String response = "";
        String clientID = "n5b4nq816vp9mc60jf78qu1e8tzd4v";
        url = "https://api.twitch.tv/kraken" + url;

       // Log.writeDebugMessage("TwitchAPIRequester.java:sendRequest", "Sending '" + requestType + "'-request to URL: " + url);

        try {
            //connect to twitch
            URL u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();

            //initialize request
            connection.setRequestMethod(requestType.name());

            connection.setRequestProperty("Accept", "application/vnd.twitchtv.v3+json");
            connection.setRequestProperty("Client-ID", clientID);

            if (!user.equals("")) {
                String token = getToken(user);
                connection.setRequestProperty("Authorization", token);
            }

            int responseCode = connection.getResponseCode();
           // Log.writeDebugMessage("TwitchAPIRequester.java:sendRequest", "Response code: " + responseCode);

            //get response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder r = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                r.append(inputLine);
            }
            in.close();

            response = r.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

       // Log.writeDebugMessage("TwitchAPIRequester.java:sendRequest", "Response: " + response);
        return response;
    }

}

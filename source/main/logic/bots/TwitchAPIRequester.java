package logic.bots;

import java.io.*;
import java.net.URL;
import java.net.HttpURLConnection;

/**
 * Created by WASABI on 15.05.2017.
 */
public class TwitchAPIRequester {

    private enum RequestType {
        GET,
        PUT,
        DELETE
    }

    /**
     * Get channel
     * @param channel Channel to get
     * @return true if channel exists
     */
    public boolean requestChannel(String channel) throws FileNotFoundException{
        //Log.writeDebugMessage("TwitchAPIRequester:requestChannel", "Request channel '" + channel + "'.");
        String url = "/channels/" + channel;
       if (sendRequest(url, RequestType.GET) >= 400) {
           return false;
       }
       return true;
    }

    /**
     * Sends a http-request of given type to the given url
     * @param url The URL which is requested.
     * @param requestType The request type of the request
     * @return server callback
     */
    private int sendRequest(String url, RequestType requestType) throws FileNotFoundException{
        return sendRequest(url, requestType, "");
    }

    /**
     * Sends a http-request of given type to the given url
     * @param url The URL which is requested.
     * @param requestType The request type of the request
     * @param user The user whose API-authenticationtoken is needed
     * @return server callback
     */
    private int sendRequest(String url, RequestType requestType, String user) throws FileNotFoundException{
        String response = "";
        String clientID = "n5b4nq816vp9mc60jf78qu1e8tzd4v";
        url = "https://api.twitch.tv/kraken" + url;


        try {
            //connect to twitch
            URL u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();

            //initialize request
            connection.setRequestMethod(requestType.name());
            connection.setRequestProperty("Accept", "application/vnd.twitchtv.v3+json");
            connection.setRequestProperty("Client-ID", clientID);

            int responseCode = connection.getResponseCode();

            return responseCode;

        } catch (IOException e) {
            System.out.println("Error while requesting " + url);
            System.exit(-1);
        }
        return -1;
    }

}

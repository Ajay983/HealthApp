package com.linkitsoft.dategoal;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class App_Socket {
    public Socket socket;
    public Emitter.Listener onNewMessage;
    public String userid;
    String message;
    {
        try {
            socket = IO.socket("https://dategoal-app.herokuapp.com/");

        } catch (URISyntaxException e) {
        }
        onNewMessage = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    message = data.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };





    }




}

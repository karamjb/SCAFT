package scaft;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.json.JSONArray;
import org.json.JSONObject;
import scaft.Models.Message;
import scaft.Models.User;
import scaft.Util.Log;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Executor;

@WebSocket
public class WebSocketSample {

    private Session session;
    private MessageTransmitter messageTransmitter;

    @OnWebSocketConnect
    public void onConnect(Session session) {
        this.session = session;
        WebSocketBroadcaster.getInstance().join(this);
    }

    @OnWebSocketMessage
    public void onJson(String message) {
        Log.e("Message",message);
        JSONObject jsonObject = new JSONObject(message);
        System.out.println(jsonObject.toString());
        switch (jsonObject.getString("messageType")){
            case MessageType.startListener:
                scaft.Session._me = new User(jsonObject.getString(MessageType.Name));
                int port = jsonObject.getInt(MessageType.PORT);
                scaft.Session._me.setPort(port);
                try {
                    if(scaft.Session.messageListener==null) {
                        scaft.Session.messageListener = new MessageListener(port);
                        scaft.Session.messageListener.start();
                    }
                    jsonObject.put(MessageType.Result, MessageType.OK);
                } catch (IOException e) {
                    e.printStackTrace();
                    jsonObject.put(MessageType.Result, MessageType.Fail);
                }
                WebSocketBroadcaster.getInstance().sendToAll(jsonObject.toString());
                break;
            case MessageType.hello:
                messageTransmitter = new MessageTransmitter(jsonObject.toString());
                messageTransmitter.start();
                break;
            case MessageType.txtMessage:
                jsonObject.put(MessageType.Result, MessageType.OK);
                //String _to = jsonObject.getString("to");
                try{
                    JSONObject jsonUser = jsonObject.getJSONObject("to");
                    User _u = scaft.Session.getUserByName(jsonUser.getString("name"));
                    messageTransmitter = new MessageTransmitter(jsonObject.toString(), _u);
                }
                catch (Exception e){
                    messageTransmitter = new MessageTransmitter(jsonObject.toString());
                }
                messageTransmitter.start();
                //  return an ack to ui
                WebSocketBroadcaster.getInstance().sendToAll(jsonObject.toString());

                break;
            case MessageType.fileRequest:
                User _u;
                try {
                    String result = jsonObject.getString(MessageType.Result);
                    try {
                        JSONObject jsonUser = jsonObject.getJSONObject("to");
                        _u = scaft.Session.getUserByName(jsonUser.getString("name"));
                    }
                    catch (Exception ex){
                        _u = scaft.Session.getUserByName(jsonObject.getString("to"));
                    }
                    if(_u!=null){
                        messageTransmitter = new MessageTransmitter(jsonObject.toString(), _u);
                        messageTransmitter.start();
                    }else{
                        //// TODO: 07/06/2017 user not found
                    }
                }
                catch (Exception e)
                {

                    try {
                        JSONObject jsonUser = jsonObject.getJSONObject("to");
                        _u =  scaft.Session.getUserByName(jsonUser.getString("name"));
                    }
                    catch (Exception ex){
                        _u = scaft.Session.getUserByName(jsonObject.getString("to"));
                    }
                    if(_u!=null){
                        messageTransmitter = new MessageTransmitter(jsonObject.toString(), _u);
                        messageTransmitter.start();
                    }else{
                        //// TODO: 07/06/2017 user not found
                    }
                }
                break;
            case MessageType.getUsersList:
                getAllUsers();
                break;
            case MessageType.fileMSG:
                JSONObject jsonUser = jsonObject.getJSONObject("to");
                messageTransmitter = new MessageTransmitter(jsonObject.toString(), scaft.Session.getUserByName(jsonUser.getString("name")));
                messageTransmitter.start();
               break;

        }
        System.out.println(jsonObject.toString());
    }

    public static void SuccessGetFile(String filePath,String from){
        JSONObject Result = new JSONObject();
        Result.put("messageType", "file");
        Result.put(MessageType.Result, MessageType.OK);
        Result.put("filePath",filePath);
        Result.put("from",from);
        Result.put("message",filePath);

        WebSocketBroadcaster.getInstance().sendToAll(Result.toString());

    }

    public static void getAllUsers() {
        JSONObject Result = new JSONObject();
        Result.put("messageType", "getUsersList");
        Result.put(MessageType.Result, MessageType.OK);
        JSONArray jsonArray = new JSONArray();
        for (User u: scaft.Session.neighbors){
            if (u.getName() == null)
                u.setName("unknown");
            JSONObject object = new JSONObject();
            object.put("ip_port", u.getIp() + ":" + u.getPort());
            if ((u.getIp()+":"+u.getPort()).equals((scaft.Session._me.getIp()+":"+scaft.Session._me.getPort()))) {
                object.put("name", "me");
                object.put("status", Status.active);
            } else {
                object.put("name", u.getName());
                object.put("status", u.getStatus());
            }
            jsonArray.put(object);
        }
        Result.put(MessageType.Users, jsonArray);
        WebSocketBroadcaster.getInstance().sendToAll(Result.toString());
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        if(scaft.Session.messageListener.isAlive())
            scaft.Session.messageListener.stop();
        WebSocketBroadcaster.getInstance().bye(this);
    }

    public Session getSession(){
        return this.session;
    }
}

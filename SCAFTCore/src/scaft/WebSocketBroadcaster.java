package scaft;

import java.util.ArrayList;
import java.util.List;

public class WebSocketBroadcaster {

    private static WebSocketBroadcaster INSTANCE = new WebSocketBroadcaster();
    private List<WebSocketSample> clients = new ArrayList<WebSocketSample>();

    private WebSocketBroadcaster(){}

    protected static WebSocketBroadcaster getInstance(){
        return INSTANCE;
    }

    protected void join(WebSocketSample socket){
        clients.add(socket);
    }

    protected void bye(WebSocketSample socket){
        clients.remove(socket);
    }

    protected void sendToAll(String message){
        for(WebSocketSample member: clients){
            member.getSession().getRemote().sendStringByFuture(message);
        }
    }

}
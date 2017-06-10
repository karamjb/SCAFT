package scaft;

import scaft.Models.User;

import java.util.List;

/**
 * Created by KARAM on 14/05/2017.
 */

public class Session {
    public static List<User> neighbors;
    public static User _me;
    public static MessageListener messageListener=null;
    public static void setHelloUser(String ip,int port,String name){
        for (User u : neighbors) {
            if (u.getIp().equals(ip)) {
                if (u.getPort() == port) {
                    u.setName(name);
                    u.setStatus(Status.active);
                }
            }
        }
    }

    public static User getUserByName(String name){
        for (User u : neighbors) {
            if (name.equals(u.getName())){
                return u;
            }
        }
        return null;
    }
    public static User getUserByIpPort(String ipPort){
        for (User u : neighbors) {
            if ((u.getIp()+":"+u.getPort()).equals(ipPort)) {
                return u;
            }
        }
        return null;
    }
}

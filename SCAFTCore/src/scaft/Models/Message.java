package scaft.Models;

/**
 * Created by Karam on 09/05/2017.
 */
public class Message {
    private String msg;
    private long date;
    private User user;

    public Message(){
    }

    public Message(String msg, long date, User user) {
        this.msg = msg;
        this.date = date;
        this.user = user;
    }

    public Message(String msg, long date) {
        this.msg = msg;
        this.date = date;
    }

    //region Getter

    public String getMsg() {
        return msg;
    }

    public long getDate() {
        return date;
    }

    public User getUser(){
        return this.user;
    }

    //endregion Getter

    //region Setter

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //endregion Setter

    //returns true on success added user and false if user is exisit
    //// TODO: 10/05/2017 remove the port if you want to run on the same machin
    /*
    public boolean addUser(User user) {
        if (this.neighbors == null) {
            this.neighbors = new ArrayList<User>();
            this.neighbors.add(user);
            return true;
        }
        for (User u : this.neighbors) {
            if ((u.getIp() + ":" + u.getPort()).equals((user.getIp() + ":" + user.getPort()))) {
                return false;
            }
        }
        return true;
    }
    */
}

package scaft.Models;

import scaft.Status;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Karam on 09/05/2017.
 */
public class User {

    private String name;
    private String ip;
    private int port;
    private String secretPassword;
    private String status = Status.Offline;

    public User(String name, String ip, int port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    public User(String ip, int port, String secretPassword) {
        this.ip = ip;
        this.port = port;
        this.secretPassword = secretPassword;
    }

    public User(String name) {
        this.name = name;
    }

    public User(){

    }

    //region Getters

    public byte[] AESKey() {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashSecretPassword = md.digest(secretPassword.getBytes(StandardCharsets.UTF_8));
            //String encoded = Base64.getEncoder().encodeToString(hashSecretPassword);
            return hashSecretPassword;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getStatus() {
        return status;
    }

    //endregion Getters

    //region Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //endregion Setters

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", secretPassword='" + secretPassword + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

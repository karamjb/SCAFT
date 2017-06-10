package scaft;
//com.kinneret.karam

import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.util.encoders.Base64;
import scaft.Models.User;
import scaft.Util.Log;
import sun.security.krb5.internal.crypto.Aes256;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.SecureRandom;

import static org.bouncycastle.asn1.cms.CMSObjectIdentifiers.encryptedData;

/**
 * Created by KARAM on 09/05/2017.
 */

public class MessageTransmitter extends Thread {

    private String message;
    private User user;

    public MessageTransmitter(String message){
        this.message = message;
        this.user = null;
    }

    public MessageTransmitter(String message,User user){
        this.message = message;
        this.user = user;
    }

    @Override
    public void run() {
        if (user == null) {
            for (User user : Session.neighbors) {
                send(user);
            }
        } else {
            send(user);
        }
    }

    private void send(User user) {
        Socket socket = null;
        byte[] encMsg = null;
        byte[] encryptedFinalData=null;
        try {
            byte[] iv = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            BouncyCastleAPI_AES_CBC bc =
                    new BouncyCastleAPI_AES_CBC(user.AESKey(),iv);
            bc.InitCiphers();

            //encryption
            byte[] encData= bc.CBCEncrypt(message.getBytes());


           ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bos.write((Session._me.getPort()+"").getBytes());
            bos.write("_".getBytes());
            bos.write(iv);
            bos.write("_".getBytes());
            bos.write(Base64.encode(encData));
            encryptedFinalData = bos.toByteArray();
            bos.close();

            //Log.e("encData Sender",new String(encData));
            if (encryptedFinalData != null) {
                encMsg = Base64.encode(encryptedFinalData);
            }


            socket = new Socket(user.getIp(), user.getPort());
            socket.getOutputStream().write(encMsg);
            socket.close();
        } catch (IOException e1) {
            Log.e("MessageTransmitter", user.getIp() + ":" + user.getPort() + " Connection refused");
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (ShortBufferException e) {
            e.printStackTrace();
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

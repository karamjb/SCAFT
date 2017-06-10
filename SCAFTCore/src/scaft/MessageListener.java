package scaft;

import org.bouncycastle.crypto.InvalidCipherTextException;
import org.json.JSONObject;
import scaft.Util.Log;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;


/**
 * Created by KARAM on 09/05/2017.
 */
public class MessageListener extends Thread {
    ServerSocket serverSocket;
    int port = 5000;

    public MessageListener(int port) throws IOException {
        this.port=port;
        serverSocket = new ServerSocket(port);
    }

    public MessageListener() throws IOException {
        new MessageListener(port);
    }

    @Override
    public void run() {
        Socket clientSocket;
        try {
            while ((clientSocket = serverSocket.accept()) != null) {
                InputStream inputStream = clientSocket.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String lin = bufferedReader.readLine();

                byte[] encMsg = org.bouncycastle.util.encoders.Base64.decode(lin.getBytes());
                String encStr = new String(encMsg);
                String ip_port = encStr.split("_")[0];
                String iv = encStr.split("_")[1];
                //Log.e("iv",iv);
                String encData = "";
                for (int i=2;i<encStr.split("_").length;i++)
                    encData = encStr.split("_")[i];


                BouncyCastleAPI_AES_CBC bc =
                        new BouncyCastleAPI_AES_CBC
                                (Session.getUserByIpPort(
                                        clientSocket.getInetAddress().getHostName()+":"+ip_port).AESKey(),iv.getBytes());
                bc.InitCiphers();
                byte[] decData = null;
                //Log.e("encData Receiver",new String(org.bouncycastle.util.encoders.Base64.decode(encData)));
                decData = bc.CBCDecrypt(org.bouncycastle.util.encoders.Base64.decode(encData.getBytes()));
                lin = new String(decData);
                //Log.e("lin",lin);

                if(lin!=null){
                    JSONObject jsonObject = new JSONObject(lin);
                    switch (jsonObject.getString("messageType")) {
                        case MessageType.hello:
                            Session.setHelloUser(clientSocket.getInetAddress().getHostName(), jsonObject.getInt("listenerPort"), jsonObject.getString("name"));
                            WebSocketSample.getAllUsers();
                            break;
                        case MessageType.fileMSG:
                            String encodedFile = jsonObject.getString("data");
                            byte[] decodedFile = Base64.getDecoder().decode(encodedFile);
                            String dirStr = "c://SCAFT/Download/" + jsonObject.getString("from") + "/" + new SimpleDateFormat("yyyyMMdd").format(new Date());
                            File dir = new File(dirStr);
                            if(dir.mkdirs()){

                            }
                            String downloadFilePath = dir.getPath() +File.separator+ jsonObject.getString("fileName");
                            FileOutputStream stream = new FileOutputStream(downloadFilePath);
                            try {
                                stream.write(decodedFile);
                                WebSocketSample.SuccessGetFile(downloadFilePath, jsonObject.getString("from"));
                                try {
                                    Desktop.getDesktop().open(new File(dirStr));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } finally {
                                stream.close();
                            }


                             /* fileToSend = {
                    messageType: "fileMSG",
                    fileName: fileName,
                    from: myName,
                    to: sendTo,
                    data: btoa(contents)
                };*/
                            break;
                    }
                   /* JSONObject jsonObject = new JSONObject(lin);
                    switch (jsonObje17-4ct.getString("messageType")){
                        case MessageType.hello:
                            for(User u:Session.neighbors){
                                if((u.getPort()+":"+u.getPort()).equals(clientSocket.getRemoteSocketAddress()+":"+clientSocket.getPort())){
                                    u.setStatus(Status.active);
                                    u.setName(jsonObject.getString(MessageType.Name));
                                    WebSocketSample.getAllUsers();

                                }
                            }
                            break;
                    }*/
                    WebSocketBroadcaster.getInstance().sendToAll(lin);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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

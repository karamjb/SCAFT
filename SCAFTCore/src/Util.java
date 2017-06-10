import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by TOSHIBA on 14/05/2017.
 */
public class Util {
    public static byte[] getAESKey(String pwd) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(pwd.getBytes("UTF-8"));
        return md.digest();
    }
}

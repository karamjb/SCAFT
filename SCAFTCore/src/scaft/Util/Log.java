package scaft.Util;

/**
 * Created by TOSHIBA on 07/06/2017.
 */
public class Log {
    public static void e(String LogTag,String Log){
        log(LogTag + " : " + Log);
    }

    private static void log(String str) {
        synchronized (System.out) {
            System.out.println(str);
        }
    }
}

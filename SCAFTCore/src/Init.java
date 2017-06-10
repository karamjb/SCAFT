import scaft.Models.User;
import scaft.Session;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by TOSHIBA on 14/05/2017.
 */

public class Init {
    public static String NEIGHBOR_FILE = "C://SCAFT/neighbors.txt";

    public static boolean Start(String neighborPath){
        NEIGHBOR_FILE = neighborPath;
        return Start();
    }
    public static boolean Start(){
        Session.neighbors = new ArrayList<User>();
        try(BufferedReader br = new BufferedReader(new FileReader(NEIGHBOR_FILE))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                Session.neighbors.add(new User(line.split(":")[0],
                        Integer.parseInt(line.split(":")[1]),
                        line.split(":")[2]));
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

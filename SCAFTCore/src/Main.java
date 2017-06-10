import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args){
        String userFileName = Init.NEIGHBOR_FILE;
        int port = 8080;
        if(args.length>0){
            port = Integer.parseInt(args[0]);
            userFileName = args[1];
            System.out.println(port);
        }
        Init.Start(userFileName);
        Server server = new Server(port);
        ResourceHandler resourceHandler = new ResourceHandler();

        WebSocketServletImpl wsservlet = new WebSocketServletImpl();
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.addServlet(new ServletHolder(wsservlet), "/");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resourceHandler, contextHandler });
        server.setHandler(handlers);

        try {
            server.start();
            System.out.println("Server Start in port:"+port);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
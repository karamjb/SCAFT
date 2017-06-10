
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import scaft.WebSocketSample;

public class WebSocketServletImpl extends WebSocketServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setMaxBinaryMessageSize(20*1024*1024);//20mb
        factory.getPolicy().setMaxTextMessageSize(20*1024*1024);//20mb
        factory.register(WebSocketSample.class);
    }
}
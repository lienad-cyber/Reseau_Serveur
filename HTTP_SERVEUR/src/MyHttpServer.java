import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

public class MyHttpServer {
    public static void main(String[] args) throws IOException {
        int port = 8000;

        InetSocketAddress inetSocketAddr = new InetSocketAddress(port);
        HttpServer httpServer = HttpServer.create(inetSocketAddr, 0);

        httpServer.createContext("/", new FileHandler());

        httpServer.setExecutor(null);
        httpServer.start();

        System.out.println("Serveur en marche : " + "localhost:" + port);
    }
}

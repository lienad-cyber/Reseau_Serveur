import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class MyHttpServer {
    public static void main(String[] args) throws IOException {
        int port = 8000;

        InetSocketAddress inetSocketAddr = new InetSocketAddress("0.0.0.0", port);
        HttpServer httpServer = HttpServer.create(inetSocketAddr, 0);

        httpServer.createContext("/", new FileHandler());

        httpServer.setExecutor(null);
        httpServer.start();

        System.out.println("Serveur en marche : http://0.0.0.0:" + port);
        System.out.println("Accessible sur l'adresse locale : http://localhost:" + port);
    }
}

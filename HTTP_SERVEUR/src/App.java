import java.io.IOException;
import java.net.*;

public class App {
    public static void main(String[] args) {
        int port = 8000;

        try (ServerSocket serverSocket = new ServerSocket(port, 50, InetAddress.getByName("0.0.0.0"));) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Connection acceptée : " + socket.getInetAddress());

                new Thread(() -> {
                    try {
                        new SocketServer().MyFileHandler(socket);
                    } catch (IOException ex) {
                    } catch (InterruptedException ex) {
                    }
                }).start();
            }
        } catch (Exception e) {
        }
    }
}

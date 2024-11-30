import java.io.IOException;
import java.net.*;

public class App {
    public static void main(String[] args) {
        int port = 8000;

        try (ServerSocket serverSocket = new ServerSocket(port);) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Connection acceptée : " + socket.getInetAddress());

                new Thread(() -> {
                    try {
                        new SocketServer().MyFileHandler(socket);
                    } catch (IOException ex) {
                    }
                }).start();
            }
        } catch (Exception e) {
        }
    }
}

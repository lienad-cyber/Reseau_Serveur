import java.io.*;
import java.net.*;

public class App {

    public static void main(String[] args) {
        int port = 8000;
        System.out.println("Serveur en marche sur le port " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connexion acceptée : " + clientSocket.getInetAddress());

                // Gérer la requête dans un thread séparé
                new Thread(() -> {
                    try {
                        new SocketServer().MyhandleRequest(clientSocket);
                    } catch (InterruptedException ex) {
                    }

                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

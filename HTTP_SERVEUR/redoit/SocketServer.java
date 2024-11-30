import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class SocketServer {
    public void MyFileHandler(Socket ClientSocket) throws IOException {
        ServerSocket serverSocket = null;
        DataOutputStream writer = null;
        BufferedReader bufferedReader = null;
        InputStreamReader DataInputStream = null;

        try {
            DataInputStream = new InputStreamReader(ClientSocket.getInputStream());
            bufferedReader = new BufferedReader(DataInputStream);
            writer = new DataOutputStream(ClientSocket.getOutputStream());

            // Le requete
            String requestString = bufferedReader.readLine();

            if (requestString == null) {
                return;
            }

            String[] requestParts = requestString.split(" ");
            // Le partie ao arinan'ny localhost:8000
            String requestedFile = requestParts[1];

            System.out.println("La requete : " + requestString);
            System.out.println("Fichier Courante : " + requestedFile);

            if (requestedFile.endsWith("/")) {
                displayListFile(requestedFile, writer);
            }

        } catch (Exception e) {
        } finally {
            DataInputStream.close();
            bufferedReader.close();
            writer.close();
        }
    }

    private void displayListFile(String currentPath, DataOutputStream writer) throws IOException {
        System.out.println("");

        File currentFile = new File("../../htdocs/ " + currentPath);

        if (currentFile.exists()) {
            if (currentFile.isDirectory()) {
                File[] listFiles = currentFile.listFiles();
                StringBuilder htmlResponse = new StringBuilder();

                htmlResponse.append("<html><body>");
                htmlResponse.append("<ul>");

                // Le fileParent

                if (listFiles != null) {
                    for (File file : listFiles) {
                        String filename = file.getName();

                        if (file.isDirectory()) {
                            htmlResponse.append("<li><a href='").append(currentPath).append("/").append(filename)
                                    .append("/").append("''>").append(filename).append("'</a></li>'");
                        } else {
                            htmlResponse.append("<li><a href='").append(currentPath).append("/").append(filename)
                                    .append("/").append("''>").append(filename).append("'</a></li>'");
                        }
                    }
                }

                htmlResponse.append("</ul>");
                htmlResponse.append("</body></html>");

                writer.writeBytes("HTTP/1.1 200 OK\r\n");
                writer.writeBytes("Content-Type: text/html\r\n");
                writer.writeBytes("Content-Length: " + htmlResponse.length() + "\r\n");
                writer.writeBytes("\r\n");

                writer.writeBytes(htmlResponse.toString());
            }
        } else {
            // DifferentHttpError.Error404(HttpExchange exchange, String message);
        }
    }
}

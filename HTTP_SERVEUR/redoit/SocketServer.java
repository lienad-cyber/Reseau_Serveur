import java.io.*;
import java.net.*;
import java.nio.file.Files;

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
                // } else if (requestedFile.endsWith(".php")) {
                // // Raha php
                // handlePHPFile(requestedFile, writer);
            } else {
                // html, css, ...
                handleStaticFile(requestedFile, writer);
            }

        } catch (Exception e) {
        } finally {
            DataInputStream.close();
            bufferedReader.close();
            writer.close();
        }
    }

    public void handleStaticFile(String currrentPath, DataOutputStream write) throws IOException {
        File file = new File("../../htdocs/" + currrentPath);

        if (file.exists()) {
            byte [] fileByte = Files.readAllBytes(file.toPath());
            String contentType = getContentType(currrentPath);

            write.writeBytes("HTTP/1.1 200 OK \r\n");
            write.writeBytes("Content-Type : "+ contentType + "\r\n");
            write.writeBytes("Content-Length : "+ fileByte.length + "\r\n");
            write.writeBytes("\r\n");

            write.write(fileByte);
        } else {
            // Error 404
            DifferentHttpError.Error404(write);
        }

    }

    private void displayListFile(String currentPath, DataOutputStream writer) throws IOException {
        System.out.println("");

        File currentFile = new File("../../htdocs/" + currentPath);

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
                            htmlResponse.append("<li><a href='").append(currentPath).append(filename)
                                    .append("/").append("''>").append(filename).append("</a></li>");
                        } else {
                            htmlResponse.append("<li><a href='").append(currentPath).append(filename)
                                    .append("''>").append(filename).append("</a></li>");
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

    private String getContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

        return switch (extension) {
            case "html" -> "text/html";
            case "css" -> "text/css";
            case "js" -> "application/javascript";
            case "png" -> "image/png";
            case "jpg", "jpeg" -> "image/jpeg";
            default -> "application/octet-stream";
        };
    }

}

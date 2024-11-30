import java.io.*;
import java.net.*;
import java.nio.file.Files;

public class SocketServer {
    public void MyFileHandler(Socket clientSocket) throws IOException, InterruptedException {

        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream writer = new DataOutputStream(clientSocket.getOutputStream())
        ) {
            // Lire la requête du client
            String requestLine = reader.readLine();
            if (requestLine == null || requestLine.isEmpty()) {
                return;
            }

            System.out.println("Requête reçue : " + requestLine);

            // Analyse de la requête
            String[] requestParts = requestLine.split(" ");

            String requestedPath = requestParts[1];
            System.out.println("Fichier demandé : " + requestedPath);

            // Gestion de la requête selon le type de fichier
            if (requestedPath.endsWith("/")) {
                displayListFile(requestedPath, writer);
            } else if (requestedPath.endsWith(".php")) {
                handlePHPFile(requestedPath, writer);
            } else {
                handleStaticFile(requestedPath, writer);
            }

        } catch (IOException e) {
            System.err.println("Erreur de communication : " + e.getMessage());
        }
    }


    public void handlePHPFile(String currrentPath, DataOutputStream write) throws IOException, InterruptedException {
        File file = new File("../../htdocs/" + currrentPath).getCanonicalFile();  // Path makany amle fichier

        if (file.exists()) {

            ProcessBuilder processBuilder = new ProcessBuilder("php", file.getAbsolutePath());
            Process process = processBuilder.start();
            int exitcode = process.waitFor();

            if (exitcode == 0) {
                try (InputStream inputStream = process.getInputStream();) {
                    byte[] fileByte = inputStream.readAllBytes();

                    write.writeBytes("HTTP/1.1 200 OK \r\n");
                    write.writeBytes("Content-Type : text/html \r\n");
                    write.writeBytes("Content-Length : " + fileByte.length + "\r\n");
                    write.writeBytes("\r\n");

                    write.write(fileByte);

                    sendResponse(write, "200 OK", "text/html", fileByte);
                } catch (Exception e) {
                }
            } else {
                DifferentHttpError.Error500(write);
            }
        } else {
            DifferentHttpError.Error404(write);
        }
    }

    public void handleStaticFile(String currrentPath, DataOutputStream write) throws IOException {
        File file = new File("../../htdocs/" + currrentPath).getCanonicalFile(); // Path makany amle fichier

        if (file.exists()) {

            byte[] fileByte = Files.readAllBytes(file.toPath());
            String contentType = getContentType(currrentPath);

            sendResponse(write, "200 OK", contentType, fileByte);
        } else {
            // Error 404
            DifferentHttpError.Error404(write);
        }
    }

    private void displayListFile(String currentPath, DataOutputStream writer) throws IOException {
        System.out.println("");

        File currentFile = new File("../../htdocs/" + currentPath).getCanonicalFile(); // Path makany amle fichier

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

                sendResponse(writer, "200 OK", "text/html", htmlResponse.toString().getBytes());
            }
        } else {
            DifferentHttpError.Error404(writer);
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

    private void sendResponse(DataOutputStream writer, String status, String contentType, byte[] content) {
        try {
            writer.writeBytes("HTTP/1.1 " + status + "\r\n");
            writer.writeBytes("Content-Type: " + contentType + "\r\n");
            writer.writeBytes("Content-Length: " + content.length + "\r\n");
            writer.writeBytes("\r\n");
            writer.write(content);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'envoi de la réponse : " + e.getMessage());
        }
    }

}

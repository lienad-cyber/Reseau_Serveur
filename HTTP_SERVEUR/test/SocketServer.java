import java.io.*;
import java.net.*;
import java.nio.file.Files;

public class SocketServer {

    public void MyhandleRequest(Socket clientSocket) throws InterruptedException {
        try {
            DataOutputStream writer;

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                writer = new DataOutputStream(clientSocket.getOutputStream());

                // Ilay requete
                String requestLine = reader.readLine();

                if (requestLine == null) {
                    return;
                }
                System.out.println("Requete : " + requestLine);

                String[] requestParts = requestLine.split(" ");
                String requestedFile = requestParts[1];

                System.out.println("request file : " + requestedFile);

                // Manampoitra liste anle fichier
                if (requestedFile.endsWith("/")) {
                    displayListFile(requestedFile, writer);
                } else if (requestedFile.endsWith(".php")) {
                    // Raha php
                    handlePHPFile(requestedFile, writer);
                } else {
                    // html, css, ...
                    handleStaticFile(requestedFile, writer);
                }
            }

            writer.close();
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayListFile(String requestedDirectory, DataOutputStream writer) throws IOException {
        File directory = new File("../../htdocs" + requestedDirectory);

        if (directory.exists() && directory.isDirectory()) {

            File[] files = directory.listFiles();
            StringBuilder htmlResponse = new StringBuilder();

            htmlResponse.append("<html><body>");
            htmlResponse.append("<h1>Index of ").append(requestedDirectory).append("</h1>");
            htmlResponse.append("<ul>");

            if (!requestedDirectory.equals("/")) {
                String parent = requestedDirectory.endsWith("/") ? requestedDirectory + ".."
                        : requestedDirectory + "/..";
                
                htmlResponse.append("<li><a href=\"").append(parent).append("\">Parent Directory</a></li>");
            }

            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    String filePath = requestedDirectory + (requestedDirectory.endsWith("/") ? "" : "/") + fileName;

                    // Afficher les fichiers et répertoires
                    if (file.isDirectory()) {
                        htmlResponse.append("<li><a href=\"").append(filePath).append("/\">[DIR] ").append(fileName)
                                .append("</a></li>");
                    } else {
                        htmlResponse.append("<li><a href=\"").append(filePath).append("\">").append(fileName)
                                .append("</a></li>");
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
        } else {

            writer.writeBytes("HTTP/1.1 404 Not Found\r\n");
            writer.writeBytes("Content-Type: text/html\r\n");
            writer.writeBytes("\r\n");
            writer.writeBytes("<html><body><h1>Erreur 404 - Répertoire non trouvé</h1></body></html>");
        }
    }

    private void handlePHPFile(String requestedFile, DataOutputStream writer) throws IOException, InterruptedException {
        File phpFile = new File("../../htdocs" + requestedFile);

        if (phpFile.exists()) {
            // Exécuter le fichier PHP via un processus externe
            ProcessBuilder processBuilder = new ProcessBuilder("php", phpFile.getAbsolutePath());
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                // Si PHP a exécuté correctement, renvoyer la sortie
                try (InputStream inputStream = process.getInputStream()) {
                    byte[] phpOutput = inputStream.readAllBytes();
                    writer.writeBytes("HTTP/1.1 200 OK\r\n");
                    writer.writeBytes("Content-Type: text/html\r\n");
                    writer.writeBytes("Content-Length: " + phpOutput.length + "\r\n");
                    writer.writeBytes("\r\n"); // Fin des en-têtes HTTP

                    writer.write(phpOutput); // Renvoyer la sortie du fichier PHP
                }
            } else {
                // En cas d'erreur dans l'exécution du fichier PHP
                writer.writeBytes("HTTP/1.1 500 Internal Server Error\r\n");
                writer.writeBytes("Content-Type: text/html\r\n");
                writer.writeBytes("\r\n");
                writer.writeBytes(
                        "<html><body><h1>Erreur 500 - Erreur lors de l'exécution du fichier PHP.</h1></body></html>");
            }
        } else {
            // Fichier PHP non trouvé
            writer.writeBytes("HTTP/1.1 404 Not Found\r\n");
            writer.writeBytes("Content-Type: text/html\r\n");
            writer.writeBytes("\r\n");
            writer.writeBytes("<html><body><h1>Erreur 404 - Fichier non trouvé</h1></body></html>");
        }
    }

    // Html, css, js, ...
    private void handleStaticFile(String requestedFile, DataOutputStream writer) throws IOException {
        File file = new File("../../htdocs" + requestedFile);

        if (file.exists()) {
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            String contentType = getContentType(requestedFile);

            writer.writeBytes("HTTP/1.1 200 OK\r\n");
            writer.writeBytes("Content-Type: " + contentType + "\r\n");
            writer.writeBytes("Content-Length: " + fileBytes.length + "\r\n");
            writer.writeBytes("\r\n"); // Fin des en-têtes HTTP

            writer.write(fileBytes); // Renvoyer le fichier statique
        } else {
            // Fichier non trouvé
            writer.writeBytes("HTTP/1.1 404 Not Found\r\n");
            writer.writeBytes("Content-Type: text/html\r\n");
            writer.writeBytes("\r\n");
            writer.writeBytes("<html><body><h1>Erreur 404 - Fichier non trouvé</h1></body></html>");
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

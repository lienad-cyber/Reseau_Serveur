import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class SocketServer {
    public void MyFileHandler(Socket clientSocket) throws IOException, InterruptedException {

        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                DataOutputStream writer = new DataOutputStream(clientSocket.getOutputStream())) {
            // Lire la requête du client
            String requestLine = reader.readLine();
            if (requestLine == null || requestLine.isEmpty()) {
                return;
            }

            System.out.println("Requête reçue : " + requestLine);

            // Analyse de la requête
            String[] requestParts = requestLine.split(" ");

            String method = requestParts[0];
            String requestedPath = requestParts[1];

            System.out.println("Fichier demandé : " + requestedPath);

            // Gestion de la requête selon le type de method
            switch (method) {
                case "GET" -> HandleGetMethod(requestedPath, writer);
                case "POST" -> HandlePostMethod(requestedPath, reader, writer);
                default -> DifferentHttpError.Error405(writer);
            }

        } catch (IOException e) {
            System.err.println("Erreur de communication : " + e.getMessage());
        }
    }

    public void HandleGetMethod(String requestedPath, DataOutputStream writer)
            throws IOException, InterruptedException {
        if (requestedPath.endsWith("/")) {
            displayListFile(requestedPath, writer);
        } else if (requestedPath.endsWith(".php")) {
            handlePHPFile(requestedPath, writer);
        } else {
            handleStaticFile(requestedPath, writer);
        }
    }

    public void handlePHPFile(String currentPath, DataOutputStream writer) throws IOException, InterruptedException {
        File file = new File("../../htdocs/" + currentPath).getCanonicalFile();

        if (file.exists()) {
            // Préparer le processus pour exécuter le fichier PHP
            ProcessBuilder processBuilder = new ProcessBuilder("php", file.getAbsolutePath());

            try {
                Process process = processBuilder.start();
                int exitCode = process.waitFor();

                if (exitCode == 0) {
                    try (InputStream inputStream = process.getInputStream()) {
                        byte[] fileBytes = inputStream.readAllBytes();
                        sendResponse(writer, "200 OK", "text/html", fileBytes);
                    } catch (IOException e) {
                        System.err.println(
                                "Erreur lors de la lecture du flux de sortie du processus : " + e.getMessage());
                        DifferentHttpError.Error500(writer);
                    }
                } else {
                    System.err.println("Le processus PHP s'est terminé avec un code d'erreur : " + exitCode);
                    DifferentHttpError.Error500(writer);
                }
            } catch (IOException e) {
                System.err.println("Erreur lors de l'exécution du fichier PHP : " + e.getMessage());
                DifferentHttpError.Error500(writer);
            } catch (InterruptedException e) {
                System.err.println("Le processus PHP a été interrompu : " + e.getMessage());
                Thread.currentThread().interrupt();
                DifferentHttpError.Error500(writer);
            }
        } else {
            DifferentHttpError.Error404(writer);
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
            case "png" -> "image/png";
            case "jpg", "jpeg" -> "image/jpeg";
            case "html" -> "text/html";
            case "css" -> "text/css";
            case "js" -> "application/javascript";
            default -> "application/octet-stream";
        };
    }

    private void HandlePostMethod(String currentPath, BufferedReader reader, DataOutputStream writer)
            throws IOException, InterruptedException {
        // Lecture de l'en-tête HTTP (Headers)
        String line;
        int contentLength = 0;
        String contentType = new String();

        while (!(line = reader.readLine()).isEmpty()) {
            System.out.println("Header : " + line);

            if (line.startsWith("Content-Length:")) {
                contentLength = Integer.parseInt(line.split(":")[1].trim());
            } else if (line.startsWith("Content-Type:")) {
                contentType = line.split(":")[1].trim();
            }
        }

        // Le corps de la requête (Données POST)
        char[] body = new char[contentLength];
        reader.read(body);
        String requestBody = new String(body);

        System.out.println("Request body : " + requestBody);
        handlePHPFileWithPostData(currentPath, requestBody, writer);
    }

    // private void handlePHPFileWithPostData(String currentPath, String
    // requestBody, int contentLength,
    // String contentType, DataOutputStream writer)
    // throws IOException, InterruptedException {
    // File file = new File("../../htdocs/" + currentPath).getCanonicalFile();

    // System.out.println("Content length : " + contentLength);

    // if (file.exists()) {
    // // Créer un processus PHP pour exécuter le fichier
    // ProcessBuilder processBuilder = new ProcessBuilder("php",
    // file.getAbsolutePath());
    // Process process = processBuilder.start();

    // // Ajouter les en-têtes HTTP dans la requête envoyée à PHP
    // try (OutputStream os = process.getOutputStream()) {
    // // Ajout des en-têtes HTTP à la requête POST
    // os.write(("POST / HTTP/1.1\r\n").getBytes(StandardCharsets.UTF_8));
    // os.write(("Content-Type: " + contentType +
    // "\r\n").getBytes(StandardCharsets.UTF_8));
    // os.write(("Content-Length: " + contentLength +
    // "\r\n").getBytes(StandardCharsets.UTF_8));
    // os.write(("\r\n").getBytes(StandardCharsets.UTF_8));
    // os.write(requestBody.getBytes(StandardCharsets.UTF_8));
    // os.flush();
    // }

    // // Attendre la fin du processus PHP et récupérer la sortie
    // int exitCode = process.waitFor();

    // // Lire la sortie d'erreur du processus PHP
    // try (InputStream errorStream = process.getErrorStream()) {
    // String errorOutput = new String(errorStream.readAllBytes(),
    // StandardCharsets.UTF_8);
    // if (!errorOutput.isEmpty()) {
    // System.err.println("Erreur PHP : " + errorOutput);
    // }
    // }

    // if (exitCode == 0) {
    // try (InputStream inputStream = process.getInputStream()) {
    // byte[] fileBytes = inputStream.readAllBytes();
    // sendResponse(writer, "200 OK", "text/html", fileBytes);
    // } catch (IOException e) {
    // System.err.println("Erreur lors de la lecture du flux de sortie du processus
    // : " + e.getMessage());
    // DifferentHttpError.Error500(writer);
    // }
    // } else {
    // System.err.println("Le processus PHP s'est terminé avec un code d'erreur : "
    // + exitCode);
    // DifferentHttpError.Error500(writer);
    // }
    // } else {
    // DifferentHttpError.Error404(writer);
    // }
    // }

    private void handlePHPFileWithPostData(String currentPath, String requestBody, DataOutputStream writer)
            throws IOException, InterruptedException {
        File file = new File("../../htdocs/" + currentPath).getCanonicalFile();

        if (file.exists()) {
            ProcessBuilder processBuilder = new ProcessBuilder("php", file.getAbsolutePath());

            // Définir les variables d'environnement nécessaires
            processBuilder.environment().put("REQUEST_METHOD", "POST");
            processBuilder.environment().put("CONTENT_TYPE", "application/x-www-form-urlencoded");
            processBuilder.environment().put("CONTENT_LENGTH", String.valueOf(requestBody.length()));

            Process process = processBuilder.start();

            // Envoyer les données POST dans le flux d'entrée
            try (OutputStream os = process.getOutputStream()) {
                os.write(requestBody.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                try (InputStream inputStream = process.getInputStream()) {
                    byte[] fileBytes = inputStream.readAllBytes();
                    sendResponse(writer, "200 OK", "text/html", fileBytes);
                }
            } else {
                // Lire et afficher les erreurs éventuelles
                try (InputStream errorStream = process.getErrorStream()) {
                    String errorOutput = new String(errorStream.readAllBytes(), StandardCharsets.UTF_8);
                    System.err.println("Erreur PHP : " + errorOutput);
                }
                DifferentHttpError.Error500(writer);
            }
        } else {
            DifferentHttpError.Error404(writer);
        }
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

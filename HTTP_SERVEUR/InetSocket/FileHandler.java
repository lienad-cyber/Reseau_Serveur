import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandler implements HttpHandler {
    DifferentesHttpError error = new DifferentesHttpError();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Ilay ao arinan'ny /localhost:8000
        String path = exchange.getRequestURI().getPath();
        String directoryPath = "../../htdocs/" + path;
        System.out.println("start : path = " + path);
        File fileToLoad = new File(directoryPath);

        System.out.println("Requête reçue de : " + exchange.getRemoteAddress());

        if (fileToLoad.exists()) {

            // Methode pour afficher la liste des fichiers
            if (fileToLoad.isDirectory()) {
                StringBuilder ListOfFileDirectory = getListOfFileDirectory(path, fileToLoad);

                exchange.getResponseHeaders().set("content-type", "text/html");
                exchange.sendResponseHeaders(200, ListOfFileDirectory.length());

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(ListOfFileDirectory.toString().getBytes());
                }

                // Mamoha anilay fichier
            } else {

                // Traitement des fichiers php
                if (getExtension(directoryPath).equals("php")) {
                    System.out.println("Php be like :)");
                    System.out.println("File to load absolute path : " + fileToLoad.getAbsolutePath());

                    ProcessBuilder processBuilder = new ProcessBuilder("php", "-f", fileToLoad.getAbsolutePath());
                    // processBuilder.redirectErrorStream(true); // Redirige les erreurs vers
                    // l'output standard pour
                    // faciliter le débogage

                    Process process = processBuilder.start();

                    int exitCode = 0;
                    try {
                        exitCode = process.waitFor();
                    } catch (InterruptedException ex) {
                    }

                    if (exitCode != 0) {
                        System.err.println("Le fichier PHP a rencontré une erreur (code : " + exitCode + ")");

                        try (InputStream errorStream = process.getErrorStream()) {
                            String errorLog = new String(errorStream.readAllBytes());
                            System.err.println("Erreur PHP : " + errorLog);
                        }

                        error.Error500(exchange, "Erreur lors de l'exécution du fichier PHP.");
                        return;
                    }

                    byte[] bytes;
                    try (InputStream is = process.getInputStream()) {
                        bytes = is.readAllBytes();
                    }

                    SendResponseToClient(exchange, "text/html", bytes);

                } else {
                    Path PathToFile = Paths.get(directoryPath);
                    byte[] fileBytes = Files.readAllBytes(PathToFile);

                    String contentType = getContentType(directoryPath);

                    SendResponseToClient(exchange, contentType, fileBytes);
                }

            }
        } else {
            // Erreur 404
            error.Error404(exchange);
            System.out.println("Tsy hitany");
        }
    }

    public void SendResponseToClient(HttpExchange exchange, String contentType, byte[] bytes) throws IOException {
        exchange.getResponseHeaders().set("content-type", contentType);

        System.out.println("exchange.getResponseHeaders().set('content-type'," + contentType + " )");
        exchange.sendResponseHeaders(200, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    public StringBuilder getListOfFileDirectory(String path, File fileToExecute) {
        StringBuilder stringBuilder = new StringBuilder();
        File[] listFilesInDirectory = fileToExecute.listFiles();
        String HostIpAddress = HostIpAddress();

        // Le localhost tokony miova anle ip anle serveur apres
        String baseUrl = "http:/" + HostIpAddress + ":8000";

        stringBuilder.append("<html><body>");
        stringBuilder.append("<ul>");

        if (!path.equals("/")) {
            String parentPath = path.substring(0, path.lastIndexOf("/"));
            stringBuilder.append("<li><a href='").append(parentPath).append("'>..</a></li>");
        }

        System.out.println("base url : "  + baseUrl +" HostIpAddress : " + HostIpAddress + " parentpath : " + path);

        for (File file : listFilesInDirectory) {

            if (file.isDirectory()) {
                stringBuilder.append("<li><a href='").append(path).append(file.getName()).append("'>")
                        .append(file.getName()).append("</a></li>");
            }

            else {
                stringBuilder.append("<li><a href='").append(path).append("/").append(file.getName())
                        .append("'>")
                        .append(file.getName()).append("</a></li>");
            }

            System.out.println("base url : "  + baseUrl + " path : " + path + " file get name : " + file.getName());
        }
        stringBuilder.append("</ul>");
        stringBuilder.append("</body></html>");

        return stringBuilder;
    }

    private String getContentType(String filePath) {
        String extension = getExtension(filePath);

        return switch (extension) {
            case "html" -> "text/html";
            case "php" -> "text/html";
            case "css" -> "text/css";
            case "js" -> "application/javascript";
            case "png" -> "image/png";
            default -> "application/octet-stream";
        };
    }

    private String getExtension(String filepath) {
        int lastDot = filepath.lastIndexOf(".");
        System.out.println("lastDot index : " + lastDot);

        return (lastDot == -1) ? "" : filepath.substring(lastDot + 1);
    }

    private String HostIpAddress() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            return localhost.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
            return "localhost";
        }
    }

}

// Post and Get
// Available am machine hafa

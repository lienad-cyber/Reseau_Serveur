import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Ilay requete
        String path = exchange.getRequestURI().getPath();
        String directoryPath = "../../htdocs/" + path;
        
        System.out.println("start : path = " + path);

        // Le fichier ao anaty htdocs
        File fileToLoad = new File(directoryPath);

        if (fileToLoad.exists()) {
            // Raha Dossier
            if (fileToLoad.isDirectory()) {
                StringBuilder ListOfFileDirectory = getListOfFileDirectory(path, fileToLoad);

                exchange.getResponseHeaders().set("content-type", "text/html");
                exchange.sendResponseHeaders(200, ListOfFileDirectory.length());

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(ListOfFileDirectory.toString().getBytes());
                }

                // Mamoha anilay fichier
            } else {
                Path PathToFile = Paths.get(directoryPath);
                byte[] fileBytes = Files.readAllBytes(PathToFile);

                String contentType = getContentType(directoryPath);

                exchange.getResponseHeaders().set("content-type", contentType);
                exchange.sendResponseHeaders(200, fileBytes.length);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(fileBytes);
                }
            }
        } else {
            // Erreur 404
            Error404(exchange);
            System.out.println("Tsy hitany");
        }
    }

    public StringBuilder getListOfFileDirectory(String path, File fileToExecute) {
        StringBuilder stringBuilder = new StringBuilder();
        File[] listFilesInDirectory = fileToExecute.listFiles();
    
        String baseUrl = "http://localhost:8000";

        stringBuilder.append("<html><body>");
        stringBuilder.append("<ul>");
        
        if (!path.equals("/")) {
            String parentPath = path.substring(0, path.lastIndexOf("/"));
            stringBuilder.append("<li><a href='").append(baseUrl).append(parentPath).append("'>..</a></li>");
        }
    
        for (File file : listFilesInDirectory) {

            if (file.isDirectory()) {
                stringBuilder.append("<li><a href='").append(baseUrl).append(path).append(file.getName()).append("'>")
                        .append(file.getName()).append("</a></li>");
            }
            
            else {
                stringBuilder.append("<li><a href='").append(baseUrl).append(path).append("/").append(file.getName()).append("'>")
                        .append(file.getName()).append("</a></li>");
            }
        }
        stringBuilder.append("</ul>");
        stringBuilder.append("</body></html>");
    
        return stringBuilder;
    }
    
    public void Error404(HttpExchange exchange) throws IOException {
        String FilePathPageNotFound = "../../htdocs/Error/Error404.html";

        Path pathToFile = Paths.get(FilePathPageNotFound);
        byte[] fileContentBytes = Files.readAllBytes(pathToFile);

        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(404, fileContentBytes.length);

        // La reponse du servera
        try (
                OutputStream os = exchange.getResponseBody()) {
            os.write(fileContentBytes);
        }
    }

    private String getContentType(String filePath) {
        String extension = getExtension(filePath);

        return switch (extension) {
            case "html" -> "text/html";
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

}
